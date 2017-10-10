package com.wxsk.vr.mine.service.impl;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.common.json.JSONResult;
import com.wxsk.mine.account.constant.Enums.CoinTypeEnum;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.constant.enums.AffectType;
import com.wxsk.vr.mine.common.constant.enums.GameBuff;
import com.wxsk.vr.mine.common.constant.enums.LandAreaAwardTypeValue;
import com.wxsk.vr.mine.common.constant.enums.LandAreaSubTypeValue;
import com.wxsk.vr.mine.common.util.CatchKeyFactory;
import com.wxsk.vr.mine.common.util.DateUtil;
import com.wxsk.vr.mine.dao.BaseDao;
import com.wxsk.vr.mine.dao.UserGameDataDao;
import com.wxsk.vr.mine.model.*;
import com.wxsk.vr.mine.properties.GameProperties;
import com.wxsk.vr.mine.service.MagicBoxService;
import com.wxsk.vr.mine.service.PageLandAreaService;
import com.wxsk.vr.mine.service.UserAccountService;
import com.wxsk.vr.mine.service.UserGameDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.wxsk.vr.mine.common.constant.enums.ServiceErrorCode.*;

@Service
public class UserGameDataServiceImpl extends BaseServiceImpl<UserGameData> implements UserGameDataService {

    private static final Logger logger = LogManager.getLogger(UserGameDataServiceImpl.class);

    @Autowired
    private UserGameDataDao userGameDataDao;

    @Autowired
    private PageLandAreaService pageLandAreaService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private MagicBoxService magicBoxService;

    @Autowired
    private GameProperties gameProperties;


    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    public UserGameDataServiceImpl (RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public UserGameData queryUserGameData(User user) throws BusinessException {
        UserGameDataDao.UserGameDataParam param = new UserGameDataDao.UserGameDataParam();
        param.setUserId(user.getId());
        List<UserGameData> userGameDataList = queryByParam(param);
        if (userGameDataList.size() == 0) {
            logger.info("no [UserGameData] found, userId: {}", user.getId());
            initUserGameData(user);
            logger.info("userId: {}, [UserGameData] has been initialized", user.getId());
            userGameDataList = queryByParam(param);
        }
        if (userGameDataList.size() > 1) {
            logger.warn("duplicated [UserGameData] in DB, userId: {}", user.getId());
        }
        return userGameDataList.get(0);
    }

    @Override
    public void initUserGameData(User user) throws BusinessException {
        UserGameData userGameData = new UserGameData();
        userGameData.setUserId(user.getId());
        userGameData.setLastConsumeEnergyTime(0);
        userGameData.setPredictEnergyFullTime(0);
        userGameData.setEnergy(gameProperties.getEnergyConfig().getMax());
        userGameData.setEmpiric(0);
        userGameData.setDigRecord(null);
        PageLandArea currentPageLandArea = pageLandAreaService.generateUserPageLandArea(user.getId(), 1);
        userGameData.setCurrentPageLandArea(currentPageLandArea);
        insert(userGameData);
    }

    @Override
    public int exchangeTimeToEnergy(long startTime, long endTime) {
        long timeInMillSecondIncreaseUnitEnergy = TimeUnit.SECONDS.toMillis(gameProperties.getEnergyConfig().getTimeInSecondIncreaseUnitEnergy());
        long increasedEnergy = (endTime - startTime) / timeInMillSecondIncreaseUnitEnergy;
        return (int) (increasedEnergy);
    }

    @Override
    public long exchangeEnergyToTime(int energy) {
        return energy * TimeUnit.SECONDS.toMillis(gameProperties.getEnergyConfig().getTimeInSecondIncreaseUnitEnergy());
    }

    @Override
    public void exchangeTimeToEnergy(UserGameData userGameData, long now) {
        long timeStart = userGameData.getLastConsumeEnergyTime();
        long timeEnd = userGameData.getPredictEnergyFullTime();
        long exchangeEndTime = timeStart;
        if (timeStart < timeEnd) {
            if (timeEnd > now) {
                timeEnd = now;
            }
            //时间兑换体力
            int exchangeEnergy = exchangeTimeToEnergy(timeStart, timeEnd);
            //成功兑换体力
            if (exchangeEnergy > 0) {
                logger.info("======================================体力兑换============================================");
                logger.info("兑换体力开始时间： {}， 兑换体力结束时间：{}，总共兑换体力：{}", DateUtil.yyyyMMddHHmmssFormat(new Date(timeStart)), DateUtil.yyyyMMddHHmmssFormat(new Date(exchangeEndTime)), exchangeEnergy);
                userGameData.setEnergy(userGameData.getEnergy() + exchangeEnergy);
                exchangeEndTime += exchangeEnergyToTime(exchangeEnergy);
            }
        }
        else if(timeStart > timeEnd){
            logger.error("userId: {} exchange time to energy error, startTime: {}, endTime: {}", userGameData.getUserId(), DateUtil.yyyyMMddHHmmssFormat(new Date(timeStart)), DateUtil.yyyyMMddHHmmssFormat(new Date(timeEnd)));
        }
        int missingEnergy = gameProperties.getEnergyConfig().getMax() - userGameData.getEnergy();
        //当前体力不超上限
        if (missingEnergy > 0) {
            userGameData.setLastConsumeEnergyTime(exchangeEndTime);
            userGameData.setPredictEnergyFullTime(exchangeEndTime + exchangeEnergyToTime(missingEnergy));
        }
        else {
            userGameData.setLastConsumeEnergyTime(exchangeEndTime);
            userGameData.setPredictEnergyFullTime(exchangeEndTime);
        }
    }

    @Override
    public int queryUserCurrentEnergy(UserGameData userGameData, long now) throws BusinessException {
        exchangeTimeToEnergy(userGameData, now);
        return userGameData.getEnergy();
    }

    @Override
    public void collectLandAreaAward(UserGameData userGameData, Map<Byte, Long> awardTypeMappingToIncrease, LandArea... landAreas) throws BusinessException {
        if (landAreas!= null && landAreas.length > 0) {
            double coefficient = 1;
            List<GameBuff> gameBuffList = userGameData.getBuffs();
            if (gameBuffList != null && !gameBuffList.isEmpty()) {
                for (GameBuff buff: gameBuffList) {
                    if (buff.getAffectType() == AffectType.HARVEST) {
                        coefficient*=buff.getCoefficient();
                    }
                }
            }
            User user = new User();
            user.setId(userGameData.getUserId());
            for (LandArea landArea: landAreas) {
                AwardType awardType = landArea.getLandAreaType().getAwardType();
                //宝箱
                if (awardType.getValue() == LandAreaAwardTypeValue.MAGIC_BOX.value) {
                    long count = magicBoxService.countUserUnusedMagicBox(user);
                    if (count < 99) {
                        MagicBox magicBox = new MagicBox();
                        magicBox.setCreateTime(landArea.getEndTime());
                        magicBox.setLandAreaIndex(landArea.getIndex());
                        magicBox.setUserId(userGameData.getUserId());
                        magicBoxService.insert(magicBox);
                    }
                    else {
                        logger.info("userId: {} magic box count: {}, ignore magic box award");
                    }
                    landArea.setActualProfitAmount(awardType.getAmount());
                }
                else {
                    Long savedAmount = awardTypeMappingToIncrease.getOrDefault(awardType.getValue(), 0L);
                    long toIncrease = (long)(awardType.getAmount() * coefficient);
                    landArea.setActualProfitAmount((int)toIncrease);
                    awardTypeMappingToIncrease.put(awardType.getValue(), savedAmount + toIncrease);
                }
            }
        }
    }


    @Override
    public Collection<AwardType> queryDigRecordLandAreaAward(User user,UserGameData userGameData, DigRecord digRecord) throws BusinessException {
        Map<Byte, AwardType> awardTypeMapping = new HashMap<>();
        if (digRecord == null) {
            return awardTypeMapping.values();
        }
        List<Integer> pageIndexes = digRecord.getPageIndex();
        if (pageIndexes != null && !pageIndexes.isEmpty()) {
            if (pageIndexes.size() > 1) {
                Collections.sort(pageIndexes);
            }
            for (int i=0; i<pageIndexes.size(); i++) {
                int pageIndex = pageIndexes.get(i);
                PageLandArea pageLandArea;
                if (userGameData.getCurrentPageLandArea() != null && userGameData.getCurrentPageLandArea().getIndex() == pageIndex) {
                    pageLandArea = userGameData.getCurrentPageLandArea();
                }
                else {
                    pageLandArea = pageLandAreaService.queryUserPageLandAreaByIndex(user, pageIndex);
                }
                if (pageLandArea == null) {
                    logger.error("userId: {}, [PageLandArea] not found, index: {}", user.getId(), pageIndex);
                    throw new BusinessException(GAME_PAGE_LAND_AREA_NOT_FOUND.msg, null, GAME_PAGE_LAND_AREA_NOT_FOUND.code);
                }
                List<LandArea> landAreaList = pageLandArea.getLandAreaList();
                int startIndex;
                int endIndex;
                if (i != 0) {
                    startIndex = 0;
                }
                else {
                    startIndex = digRecord.getLandAreaStartIndex();
                }
                if (i != pageIndexes.size() - 1) {
                    endIndex = landAreaList.size();
                }
                else {
                    if (digRecord.getEndTime() == 0) {
                        endIndex = userGameData.getCurrentLandArea().getIndex() + 1;
                    }
                    else {
                        endIndex = digRecord.getLandAreaEndIndex() + 1;
                    }
                }
                for (; startIndex< endIndex; startIndex++) {
                    LandArea landArea = landAreaList.get(startIndex);
                    if (landArea.getEndTime() != 0) {
                        AwardType awardType = landArea.getLandAreaType().getAwardType();
                        AwardType saved = awardTypeMapping.get(awardType.getValue());
                        if (saved == null) {
                            try {
                                AwardType clonedAwardType = (AwardType)awardType.clone();
                                clonedAwardType.setAmount(landArea.getActualProfitAmount());
                                awardTypeMapping.put(awardType.getValue(), clonedAwardType);
                            } catch (CloneNotSupportedException e) {
                                logger.error("AwardType Clone Exception", e);
                                throw new BusinessException(GAME_SERVER_INTERNAL_EXCEPTION.msg, null, GAME_SERVER_INTERNAL_EXCEPTION.code);
                            }
                        }
                        else {
                            saved.setAmount(saved.getAmount() + landArea.getActualProfitAmount());
                        }
                    }
                }
            }
        }
        return awardTypeMapping.values();
    }

    @Override
    public LandArea queryCurrentLandArea(User user, long now) throws BusinessException {
        UserGameData userGameData = queryUserGameData(user);
        LandArea currentLandArea = userGameData.getCurrentLandArea();
        if (currentLandArea == null) {
            return null;
        }
        if (currentLandArea.getEndTime() <= now) {
            pageLandAreaService.synchronizedPageLandArea(user, now);
            userGameData = queryUserGameData(user);
        }
        return userGameData.getCurrentLandArea();
    }

    @Override
    public void addGameBuff(UserGameData userGameData, GameBuff gameBuff) {
        if (gameBuff != null) {
            List<GameBuff> buffList = userGameData.getBuffs();
            if (buffList == null) {
                buffList = new ArrayList<>(1);
                userGameData.setBuffs(buffList);
                buffList.add(gameBuff);
            }
            else {
                if (buffList.size() == 0) {
                    buffList.add(gameBuff);
                }
                else {
                    if (gameBuff != GameBuff.RAT_BUFF) {
                        boolean containsRatBuff = buffList.contains(GameBuff.RAT_BUFF);
                        buffList.clear();
                        buffList.add(gameBuff);
                        if (containsRatBuff) {
                            buffList.add(GameBuff.RAT_BUFF);
                        }
                    }
                    else {
                        if (!buffList.contains(gameBuff)) {
                            buffList.add(gameBuff);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removeGameBuff(UserGameData userGameData, GameBuff gameBuff) {
        if (gameBuff != null) {
            List<GameBuff> buffList = userGameData.getBuffs();
            if (buffList!= null && !buffList.isEmpty()) {
                buffList.remove(gameBuff);
            }
        }
    }

    @Override
    public void triggerVolcanoBuff(User user, long now) throws BusinessException {
        UserGameData userGameData = queryUserGameData(user);
        DigRecord digRecord = userGameData.getDigRecord();
        if (digRecord == null || digRecord.getEndTime() != 0) {
            logger.info("当前不再挂机中， 触发火山buff失败");
            throw new BusinessException(GAME_CLIENT_INVILAD_REQUEST.msg, null, GAME_CLIENT_INVILAD_REQUEST.code);
        }
        LandArea currentLandArea = userGameData.getCurrentLandArea();
        //同步地块信息
        if (currentLandArea.getEndTime() < now) {
            pageLandAreaService.synchronizedPageLandArea(user, now);
            userGameData = queryUserGameData(user);
            currentLandArea = userGameData.getCurrentLandArea();
        }
        //如果不为火山类型
        if (currentLandArea.getLandAreaType().getSubType() != LandAreaSubTypeValue.VOLCANO_BLOCK.getValue()) {
            logger.info("当前地块不为火山, 触发火山buff失败, 实际地块: {}", currentLandArea);
            throw new BusinessException(GAME_CLIENT_INVILAD_REQUEST.msg, null, GAME_CLIENT_INVILAD_REQUEST.code);
        }
        //设置火山buff
        addGameBuff(userGameData, GameBuff.getGameBuff(currentLandArea.getLandAreaType().getSubType()));
        currentLandArea.setEndTime(now);
        //更新地块信息
        List<LandArea> landAreaList = userGameData.getCurrentPageLandArea().getLandAreaList();
        landAreaList.set(currentLandArea.getIndex(), currentLandArea);
        updateUserGameData(userGameData);
    }

    @Override
    public void removeSwagBuff(User user, long now) throws BusinessException {
        UserGameData userGameData = queryUserGameData(user);
        DigRecord digRecord = userGameData.getDigRecord();
        if (digRecord == null || digRecord.getEndTime() != 0) {
            logger.info("当前不再挂机中， 取消水潭buff失败");
            throw new BusinessException(GAME_CLIENT_INVILAD_REQUEST.msg, null, GAME_CLIENT_INVILAD_REQUEST.code);
        }
        LandArea currentLandArea = userGameData.getCurrentLandArea();
        //同步地块信息
        if (currentLandArea.getEndTime() < now) {
            pageLandAreaService.synchronizedPageLandArea(user, now);
            userGameData = queryUserGameData(user);
        }
        //移除水潭buff
        removeGameBuff(userGameData, GameBuff.SWAG_BUFF);
        updateUserGameData(userGameData);
    }

    @Override
    public void removePlantBuff(User user, long now) throws BusinessException {
        UserGameData userGameData = queryUserGameData(user);
        DigRecord digRecord = userGameData.getDigRecord();
        if (digRecord == null || digRecord.getEndTime() != 0) {
            logger.info("当前不再挂机中， 取消藤蔓buff失败");
            throw new BusinessException(GAME_CLIENT_INVILAD_REQUEST.msg, null, GAME_CLIENT_INVILAD_REQUEST.code);
        }
        LandArea currentLandArea = userGameData.getCurrentLandArea();
        //同步地块信息
        if (currentLandArea.getEndTime() < now) {
            pageLandAreaService.synchronizedPageLandArea(user, now);
            userGameData = queryUserGameData(user);
        }
        //移除水潭buff
        removeGameBuff(userGameData, GameBuff.PLANT_BUFF);
        updateUserGameData(userGameData);
    }

    @Override
    public void removeRatBuff(User user, long now) throws BusinessException {
        UserGameData userGameData = queryUserGameData(user);
        DigRecord digRecord = userGameData.getDigRecord();
        if (digRecord == null || digRecord.getEndTime() != 0) {
            logger.info("当前不再挂机中， 取消老鼠buff失败");
            throw new BusinessException(GAME_CLIENT_INVILAD_REQUEST.msg, null, GAME_CLIENT_INVILAD_REQUEST.code);
        }
        LandArea currentLandArea = userGameData.getCurrentLandArea();
        //同步地块信息
        if (currentLandArea.getEndTime() < now) {
            pageLandAreaService.synchronizedPageLandArea(user, now);
            userGameData = queryUserGameData(user);
        }
        JSONResult jsonResult = userAccountService.deductionCoin(user, 1000, CoinTypeEnum.GOLDCOIN, "喂老鼠解buff");
        if (jsonResult.getStatus() != 1) {
            if ("ACCOUNT_0002".equals(jsonResult.getErrorCode())) {
                throw new BusinessException(GAME_GOLD_COIN_NOT_ENOUGH.msg, null, GAME_GOLD_COIN_NOT_ENOUGH.code);
            }
            else {
                throw new BusinessException(GAME_SERVER_INTERNAL_EXCEPTION.msg, null, GAME_SERVER_INTERNAL_EXCEPTION.code);
            }
        }
        //移除老鼠buff
        removeGameBuff(userGameData, GameBuff.RAT_BUFF);
        updateUserGameData(userGameData);
    }

    @Override
    public List<GameBuff> queryUserGameBuff(User user, long now) throws BusinessException {
        //同步地块信息
        pageLandAreaService.synchronizedPageLandArea(user, now);
        UserGameData userGameData = queryUserGameData(user);
        return userGameData.getBuffs();
    }

    @Override
    public long receiveEnergyDaily(User user, long now) throws BusinessException, ParseException {
        UserGameData userGameData = queryUserGameData(user);
        GameProperties.EnergyConfig energyConfig = gameProperties.getEnergyConfig();
        int energy = queryUserCurrentEnergy(userGameData, now);
        int maxEnergy = energyConfig.getMax();
        int maxReceiveEnergyInTimeRange = energyConfig.getMaxReceiveEnergyInEachTimeRange();
        long addEnergy = 0;
        if (energy == maxEnergy) {
            return addEnergy;
        }
        int maxReceiveEnergy = maxEnergy - energy;
        Date date = new Date();
        List<ReceiveEnergy> receiveEnergyList = gameProperties.getReceiveEnergyList();
        for (ReceiveEnergy receiveEnergy : receiveEnergyList) {
            Date start = dateFormat(receiveEnergy.getStartTime());
            Date end = dateFormat(receiveEnergy.getEndTime());
            int num = 0;
            if (date.before(end) && date.after(start)) {
                num = receiveEnergy.getNum();
                String key = CatchKeyFactory.getKeyOfReceiveEnergyDaily(user.getId(), num);
                Long value = valueOperations.increment(key, maxReceiveEnergy);
                redisTemplate.expire(key, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
                if (value > maxReceiveEnergyInTimeRange) {
                    addEnergy(user, maxReceiveEnergy - (value.intValue() - maxReceiveEnergyInTimeRange), now);
                    valueOperations.set(key, Integer.toString(maxReceiveEnergyInTimeRange));
                    addEnergy = maxReceiveEnergy - (value.intValue() - maxReceiveEnergyInTimeRange);
                } else {
                    addEnergy(user, maxReceiveEnergy, now);
                    addEnergy = maxReceiveEnergy;
                }
                return addEnergy;
            }
        }
        throw new BusinessException(GAME_DAILY_RECIEVE_ENERGY_OUT_OF_RANGE.msg, null, GAME_DAILY_RECIEVE_ENERGY_OUT_OF_RANGE.code);
    }

    @Override
    public void updateUserGameData(UserGameData userGameData) {
        userGameDataDao.update(userGameData);
    }

    @Override
    public List<UserGameData> queryUserGameDataByParam(UserGameDataDao.UserGameDataParam userGameDataParam) {
        return userGameDataDao.queryByParam(userGameDataParam);
    }

    /**
     * 字符串(HH:mm:dd)转成日期
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
    public int addEnergy(User user,int energyCount, long now) throws BusinessException {
        UserGameData userGameData = queryUserGameData(user);
        long mills = TimeUnit.SECONDS.toMillis(gameProperties.getEnergyConfig().getTimeInSecondIncreaseUnitEnergy());
        long energyTime = energyCount * mills ;
        long lastConsumeEnergyTime = userGameData.getLastConsumeEnergyTime() ;
        userGameData.setLastConsumeEnergyTime(lastConsumeEnergyTime - energyTime);
        //获取最体力值
        int maxEnergy = gameProperties.getEnergyConfig().getMax();
        int energy = (int)((now - lastConsumeEnergyTime) / mills);
        if (energy > maxEnergy) {
            energy = maxEnergy;
        }
        //返回剩余体力
        return energy;
    }

    @Override
    public BaseDao<UserGameData> getBaseDao() {
        return userGameDataDao;
    }
}
