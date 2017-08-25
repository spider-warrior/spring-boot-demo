package com.wxsk.vr.mine.service.impl;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.common.json.JSONResult;
import com.wxsk.passport.model.User;
import com.wxsk.vr.account.constant.Enums.CoinTypeEnum;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.common.util.CatchKeyFactory;
import com.wxsk.vr.mine.config.GameConfig;
import com.wxsk.vr.mine.model.*;
import com.wxsk.vr.mine.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.wxsk.vr.mine.common.constant.enums.ServiceErrorCode.*;

@Service
public class UserGameDataServiceImpl extends BaseServiceImpl<UserGameData> implements UserGameDataService {

    private static final Logger logger = LogManager.getLogger(UserGameDataServiceImpl.class);

    /** 每时间段领取体力最大值  */
    private static final Integer DAILY_RECIEVE_MAX_ENERGY = 50;
    
    /** 一天时间  */
    private static final int DAY_TIME = 24*60*60;

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    public UserGameDataServiceImpl (RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        valueOperations = redisTemplate.opsForValue();
    }
    
    @Autowired
    private UserLandAreaService userLandAreaService;
    @Autowired
    private GameConfig gameConfig;
    @Autowired
    private UserGameDataService userGameDataService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private PageLandAreaService pageLandAreaService;
    @Autowired
    private DigRecordService digRecordService;


    @Override
    public UserGameData queryUserGameData(User user) {
        Query query = new Query();
        query.addCriteria(new Criteria("userId").is(user.getId()));
        List<UserGameData> userGameDataList = this.queryByParam(query);
        if (userGameDataList.size() == 0) {
            logger.info("no [UserGameData] found, username: {}", user.getUsername());
            initUserGameData(user);
            logger.info("username: {}, [UserGameData] has been initialized", user.getUsername());
            userGameDataList = this.queryByParam(query);
        }
        if (userGameDataList.size() > 1) {
            logger.warn("duplicated [UserGameData] in DB, username: {}", user.getUsername());
        }
        return userGameDataList.get(0);
    }

    @Override
    public void updateUserGameData(UserGameData userGameData) {
        Query query = new Query();
        query.addCriteria(new Criteria("_id").is(userGameData.getId()));
        Update update = Update.update("lastConsumeEnergyTime", userGameData.getLastConsumeEnergyTime());
        update.set("digRecord", userGameData.getDigRecord());
        update.set("jiguangId", userGameData.getJiguangId());
        updateFirst(query, update);
    }

    /**
     * 前置逻辑: 判断当前用户是否在进行中
     * 挖矿逻辑:
     *      1.消耗体力   ----> 更新上次消耗体力时间为当前时间
     *      2.挖矿      ---->  根据已生成的矿地标记所有本次可挖掘的地块
     *      3.记录本次挖掘完毕时间入redis, 记录挖矿结束时间,以及用户ID(该逻辑为手机推送使用)
     * */
    @Override
    public void consumeAllEnergy4Mining(User user) throws BusinessException {

        //1.前置逻辑
        UserGameData userGameData = queryUserGameData(user);
        DigRecord digRecord = userGameData.getDigRecord();
        if (digRecord != null && digRecord.getEndTime() > AppContext.getCurrentRequestTimePoint().getTime()) {
            throw new BusinessException(GAME_ALREADY_IN_MINING.msg, null, GAME_ALREADY_IN_MINING.code);
        }
        //2.挖矿
        //可消耗体力
        int energy = queryUserEnergy(userGameData);
        if (energy == 0) {
            throw new BusinessException(ENERGY_NOT_ENOUGH.msg, null, ENERGY_NOT_ENOUGH.code);
        }
        UserLandArea userLandArea = userLandAreaService.queryUserLandArea(user);
        long startTime = AppContext.getCurrentRequestTimePoint().getTime();
        DigRecord newDigRecord = userLandAreaService.consumeEnergyOnLandAreaList(energy, startTime, userLandArea.getCurrentLandAreas(), userLandArea.getNextLandAreas());
        //持久话挖掘记录
        if (digRecord != null) {
            digRecord.setUserId(user.getId());
            digRecordService.insert(digRecord);
        }
        userGameData.setDigRecord(newDigRecord);
        userGameData.setLastConsumeEnergyTime(System.currentTimeMillis());

        PageLandArea currentPageLandArea = pageLandAreaService.queryUserPageLandAreaByIndex(user.getId(), userLandArea.getCurrentLandAreas().getIndex());
        currentPageLandArea.setLandAreaList(userLandArea.getCurrentLandAreas().getLandAreaList());
        pageLandAreaService.updatePageLandArea(currentPageLandArea);
        //累积经验值
        int empiric = UserGameData.reckonEnergyToEmpiric(energy);
        userGameData.setEmpiric(userGameData.getEmpiric() + empiric);
        //更新用户游戏数据
        updateUserGameData(userGameData);
        //更新用户地块数据
        userLandAreaService.updateUserLandArea(userLandArea);
        // TODO: 7/19/17 向redis写入结束时间调度轮循做挖矿结束收益推送

    }

    /**
     * 初始化用户数据
     * @param user 用户数据
     * */
    @Override
    public void initUserGameData(User user) {
        UserGameData userGameData = new UserGameData();
        userGameData.setUserId(user.getId());
        userGameData.setLastConsumeEnergyTime(0);
        insert(userGameData);
    }

    @Override
    public int queryUserEnergy(UserGameData userGameData) {
        long lastConsumeEnergyTime = userGameData.getLastConsumeEnergyTime();
        int maxEnergy = gameConfig.getEnergyConfig().getMax();
        int energy;
        if (lastConsumeEnergyTime == 0) {
            energy = maxEnergy;
        }
        else {
            //计算可用体力
            long mills = TimeUnit.SECONDS.toMillis(gameConfig.getEnergyConfig().getTimeInSecondIncreaseUnitEnergy());
            long now = AppContext.getCurrentRequestTimePoint().getTime();
            energy = (int)((now - lastConsumeEnergyTime) / mills);
            if (energy > maxEnergy) {
                energy = maxEnergy;
            }
        }
        return energy;
    }

    @Override
    public Date queryMineEndTime(User user) {
        UserGameData userGameData = queryUserGameData(user);
        DigRecord digRecord = userGameData.getDigRecord();
        if (digRecord == null || digRecord.getEndTime() == 0) {
            return new Date();
        }
        else {
            return new Date(digRecord.getEndTime());
        }
    }

    @Override
    public int addEnergy(User user,int energyCount){
    	UserGameData userGameData = userGameDataService.queryUserGameData(user);
    	long mills = TimeUnit.SECONDS.toMillis(gameConfig.getEnergyConfig().getTimeInSecondIncreaseUnitEnergy());
    	long energyTime = energyCount * mills ;
    	long lastConsumeEnergyTime = userGameData.getLastConsumeEnergyTime() ;
    	userGameData.setLastConsumeEnergyTime(lastConsumeEnergyTime - energyTime);
    	//获取最体力值
    	int maxEnergy = gameConfig.getEnergyConfig().getMax();
    	long now = AppContext.getCurrentRequestTimePoint().getTime();
        int energy = (int)((now - lastConsumeEnergyTime) / mills);
        if (energy > maxEnergy) {
            energy = maxEnergy;
        }
        //返回剩余体力
		return energy;
    }

    @Override
	public PageLandArea flushArea(User user, int index) throws BusinessException {
		UserLandArea userLandArea = userLandAreaService.queryUserLandArea(user);
        if(userLandArea == null){
        	throw new BusinessException(GAME_NO_USER_LAND_AREA.msg, null, GAME_NO_USER_LAND_AREA.code);
        }
        PageLandArea pageLandArea = null;
        if(index == 0){
        	pageLandArea = userLandArea.getCurrentLandAreas();
        }else{
        	pageLandArea = userLandArea.getNextLandAreas();
        }
        if(pageLandArea == null){
        	throw new BusinessException(GAME_NO_USER_LAND_AREA.msg, null, GAME_NO_USER_LAND_AREA.code);
        }
        LandArea firstLandArea = pageLandArea.getLandAreaList().get(0);
        
        if(firstLandArea.getEndTime() > 0 && LandArea.reckonStartTime(firstLandArea) <= System.currentTimeMillis()){
        	throw new BusinessException(GAME_ALREADY_IN_MINING.msg, null, GAME_ALREADY_IN_MINING.code);
        }
        
        //
        int flushCnt = pageLandArea.getFlushCnt()+1;
        if(flushCnt > 12){
        	throw new BusinessException(GAME_PAGE_LAND_AREA_FLUSH_LIMIT.msg, null, GAME_PAGE_LAND_AREA_FLUSH_LIMIT.code);
        }
        // 计算所需钻石
        Long damonNeed = reckonDamondsByFlushCnt(flushCnt);
        // 尝试扣减钻石
        JSONResult result = userAccountService.deductionCoin(user, damonNeed, CoinTypeEnum.DIAMOND, "第"+flushCnt+"次刷新矿区" + pageLandArea.getIndex());  
        if (JSONResult.STATUS_SUCCESS != result.getStatus()) {
            throw new BusinessException(result.getMessage(), null, result.getErrorCode());
        }
        PageLandArea pageLandAreaNew =  pageLandAreaService.randomLandArea(29, flushCnt);
        // 上次该矿区已消耗的体力，需要兑换到新矿区
        Integer preEnergy = pageLandAreaService.sumContainEnergyOfPageLandArea(pageLandArea);
        if(preEnergy >= 0){
        	Long startTime = pageLandAreaService.lastTimeOfPageLandArea(userLandArea.getCurrentLandAreas());
            userLandAreaService.consumeEnergyOnLandAreaList(preEnergy, startTime, pageLandAreaNew);
        }
        if(index == 0){
        	userLandArea.setCurrentLandAreas(pageLandAreaNew);
        }else{
        	userLandArea.setNextLandAreas(pageLandAreaNew);
        }
        pageLandAreaNew.setFlushCnt(flushCnt);
        pageLandAreaNew.setIndex(pageLandArea.getIndex());
        pageLandAreaNew.setUserId(pageLandArea.getUserId());
        userLandAreaService.updateUserLandArea(userLandArea);
        return pageLandAreaNew;
	}
	
	@Override
    public long reckonDamondsByFlushCnt(int cnt){
		if(cnt == 0){ return 0; }
    	if(cnt>=1 && cnt <=3){ return 10; }
    	if(cnt>=4 && cnt <=6){ return 30; }
    	if(cnt>=7 && cnt <=9){ return 50; }
    	if(cnt>=10 && cnt <=12){ return 80; }
    	return 1000000000;
    }
	
	@Override
	public long receiveEnergyDaily(User user) throws BusinessException, ParseException {
		UserGameData userGameData = userGameDataService.queryUserGameData(user);
        int energy = userGameDataService.queryUserEnergy(userGameData);
        int maxEnergy = gameConfig.getEnergyConfig().getMax();
        long addEnergy = 0;
        if (energy == maxEnergy) {
        	return addEnergy;
        }
        //
        int maxRecieveEnergy = maxEnergy - energy;
		Date date = new Date();
		List<ReceiveEnergy> recieveEnergyList = gameConfig.getReceiveEnergyList();
		for (ReceiveEnergy recieveEnergy : recieveEnergyList) {
			Date start = dateFormat(recieveEnergy.getStartTime());
			Date end = dateFormat(recieveEnergy.getEndTime());
			int num = 0;
			if (date.before(end) && date.after(start)) {
				num = recieveEnergy.getNum();
				String key = CatchKeyFactory.getKeyOfReceiveEnergyDaily(user.getId(), num);
                Long value = valueOperations.increment(key, maxRecieveEnergy);
                redisTemplate.expire(key, DAY_TIME, TimeUnit.SECONDS);
				if (value > DAILY_RECIEVE_MAX_ENERGY) {
					userGameDataService.addEnergy(user, maxRecieveEnergy - (value.intValue() - DAILY_RECIEVE_MAX_ENERGY));
                    valueOperations.set(key, Integer.toString(DAILY_RECIEVE_MAX_ENERGY));
					addEnergy = maxRecieveEnergy - (value.intValue() - DAILY_RECIEVE_MAX_ENERGY);
				} else {
					userGameDataService.addEnergy(user, maxRecieveEnergy);
					addEnergy = maxRecieveEnergy;
				}
				return addEnergy;
			}
		}
		throw new BusinessException(GAME_DAILY_RECIEVE_ENERGY_OUT_OF_RANGE.msg, null, GAME_DAILY_RECIEVE_ENERGY_OUT_OF_RANGE.code);
	}
	
	/**
	 * 比较两个时间字符串(HH:mm:dd)大小
	 * @param dateFirst
	 * @param dateSecond
	 * @return 1大于，-1小于，0等于
	 */
	public static int compareTime (String dateFirst , String dateSecond) {
		String [] sf = dateFirst.split(":");
		String [] ss = dateSecond.split(":");
		if (Integer.parseInt(sf[0]) > Integer.parseInt(ss[0])) {
			return 1;
		} else if (Integer.parseInt(sf[0]) < Integer.parseInt(ss[0])) {
			return -1;
		} else {
			if (Integer.parseInt(sf[1]) > Integer.parseInt(ss[1])) {
				return 1;
			} else if (Integer.parseInt(sf[1]) < Integer.parseInt(ss[1])) {
				return -1;
			} else {
				if (Integer.parseInt(sf[2]) > Integer.parseInt(ss[2])) {
					return 1;
				} else if (Integer.parseInt(sf[2]) < Integer.parseInt(ss[2])) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * 字符串(HH:mm:dd)转成日期
	 * @param dateFirst
	 * @throws ParseException 
	 */
	public static Date dateFormat (String timeString) throws ParseException {
		SimpleDateFormat sdfOfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfOfDate = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String dateString = sdfOfDate.format(now);
		String dateAndTime = dateString + " " + timeString;
		Date date = sdfOfTime.parse(dateAndTime);
		return date;
	}
	
    @Override
    public List<UserGameData> queryUserGameDataByParam(UserGameDataParam userGameDataParam) {
        return queryByParam(userGameDataParam.buildQuery());
    }

    public static void main(String[] args) throws ParseException {
    	String t1 = "20:12:30";
    	String t2 = "20:12:30";
    	Date now = new Date();
		System.out.println(now.after(dateFormat(t1))+" "+now.before(dateFormat(t2)));
    	System.out.println(dateFormat("12:09:32"));
	}

}
