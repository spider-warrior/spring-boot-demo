package com.wxsk.vr.mine.service.impl;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.constant.enums.LandAreaAwardTypeValue;
import com.wxsk.vr.mine.common.constant.enums.LandAreaSubTypeValue;
import com.wxsk.vr.mine.common.util.AlgorithmUtil;
import com.wxsk.vr.mine.common.util.DateUtil;
import com.wxsk.vr.mine.dao.BaseDao;
import com.wxsk.vr.mine.dao.PageLandAreaDao;
import com.wxsk.vr.mine.model.*;
import com.wxsk.vr.mine.properties.GameProperties;
import com.wxsk.vr.mine.service.LandAreaService;
import com.wxsk.vr.mine.service.PageLandAreaService;
import com.wxsk.vr.mine.service.UserAccountService;
import com.wxsk.vr.mine.service.UserGameDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.wxsk.vr.mine.common.constant.enums.ServiceErrorCode.GAME_SERVER_INTERNAL_EXCEPTION;

@Service
public class PageLandAreaServiceImpl extends BaseServiceImpl<PageLandArea> implements PageLandAreaService {

    private static final Logger logger = LogManager.getLogger(PageLandAreaServiceImpl.class);

    @Autowired
    private PageLandAreaDao pageLandAreaDao;

    @Autowired
    private UserGameDataService userGameDataService;
    @Autowired
    private LandAreaService landAreaService;
    @Autowired
    private PageLandAreaService pageLandAreaService;
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private GameProperties gameProperties;

    @Override
    public void synchronizedPageLandArea(User user, long now) throws BusinessException {
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        DigRecord digRecord = userGameData.getDigRecord();
        LandArea currentLandArea = userGameData.getCurrentLandArea();
        //非挂机状态不需要同步数据 || 当前地块未结束
        if (digRecord == null || digRecord.getEndTime() !=0 || currentLandArea.getEndTime() > now) {
            return;
        }
        //奖励
        Map<Byte, Long> awardTypeMappingToIncrease = new HashMap<>();
        if (currentLandArea.getActualProfitAmount() == 0) {
            userGameDataService.collectLandAreaAward(userGameData, awardTypeMappingToIncrease, currentLandArea);
            userGameData.getCurrentPageLandArea().getLandAreaList().set(currentLandArea.getIndex(), currentLandArea);
        }
        //兑换可用体力
        int startIndex;
        if (currentLandArea.getIndex() == userGameData.getCurrentPageLandArea().getLandAreaList().size() - 1) {
            startIndex = 0;
            //当前地块挖掘完毕,持久化
            pageLandAreaService.insert(userGameData.getCurrentPageLandArea());
            userGameData.setCurrentPageLandArea(pageLandAreaService.generateUserPageLandArea(user.getId(), userGameData.getCurrentPageLandArea().getIndex() + 1));
        }
        else {
            startIndex = currentLandArea.getIndex() + 1;
        }
        long timeMark = currentLandArea.getEndTime();
        PageLandArea currentPageLandArea = userGameData.getCurrentPageLandArea();
        List<LandArea> currentLandAreaList = currentPageLandArea.getLandAreaList();
        logger.info("开始同步地块数据, 当前体力: {}, 上个地块 索引： {}， start time: {}, end time: {}", userGameData.getEnergy(),currentLandArea.getIndex(), DateUtil.yyyyMMddHHmmssFormat(new Date(currentLandArea.getStartTime())), DateUtil.yyyyMMddHHmmssFormat(new Date(currentLandArea.getEndTime())));
        for (int i=startIndex; i<currentLandAreaList.size(); i++ ) {
            LandArea this_ = currentLandAreaList.get(i);
            int requiredEnergy = landAreaService.getLandAreaRequireEnergy(userGameData, this_);
            //体力不足,兑换体力
            if (userGameData.getEnergy() < requiredEnergy) {
                userGameDataService.exchangeTimeToEnergy(userGameData, timeMark);
            }
            //体力不足本次挖矿结束
            if (userGameData.getEnergy() < requiredEnergy) {
                if (i != 0) {
                    digRecord.setLandAreaEndIndex(i-1);
                    digRecord.setEndTime(timeMark);
                }
                else {
                    PageLandArea pageLandArea = pageLandAreaService.queryUserPageLandAreaByIndex(user, currentPageLandArea.getIndex() - 1);
                    List<LandArea> lastLandAreaList = pageLandArea.getLandAreaList();
                    LandArea lastLandArea = lastLandAreaList.get(lastLandAreaList.size() - 1);
                    digRecord.setLandAreaEndIndex(lastLandArea.getIndex());
                    digRecord.setEndTime(lastLandArea.getEndTime());
                }
                //清理用户buff
                if (userGameData.getBuffs() != null) {
                    userGameData.getBuffs().clear();
                }
                break;
            }
            currentLandArea = this_;
            landAreaService.consumeEnergyOnLandArea(userGameData, currentLandArea, userGameData.getEnergy(), timeMark);
            userGameData.setEnergy(userGameData.getEnergy() - requiredEnergy);
            if (requiredEnergy > 0) {
                //更新满体力时间
                userGameData.setPredictEnergyFullTime(userGameData.getPredictEnergyFullTime() + userGameDataService.exchangeEnergyToTime(requiredEnergy));
            }
            if (currentLandArea.getIndex() == 0) {
                digRecord.addPageIndex(currentPageLandArea.getIndex());
            }
            //当前挖掘的地块超过当前时间,所有地块已结束同步
            timeMark = currentLandArea.getEndTime();
            if (timeMark > now) {
                break;
            }
            else {
                userGameDataService.collectLandAreaAward(userGameData, awardTypeMappingToIncrease, currentLandArea);
                //涨经验
                userGameData.setEmpiric(userGameData.getEmpiric() + currentLandArea.getLandAreaType().getExp());
            }
        }
        userGameData.setCurrentLandArea(currentLandArea);
        userGameDataService.updateUserGameData(userGameData);
        //累计收益
        if (awardTypeMappingToIncrease.size() > 0) {
            for (Map.Entry<Byte, Long> entry: awardTypeMappingToIncrease.entrySet()) {
                if (entry.getValue() > 0) {
                    userAccountService.increaseAccount(user, LandAreaAwardTypeValue.getLandAreaAwardTypeValue(entry.getKey()), entry.getValue());
                }
            }
        }

        //重新计算体力
        if (timeMark < now) {
            synchronizedPageLandArea(user, now);
        }
    }

    @Override
    public PageLandArea generateUserPageLandArea(Long userId, int index) throws BusinessException {
        PageLandArea pageLandArea = randomLandArea(gameProperties.getLandAreaConfig().getSizeOfPageLandArea(), 0);
//        PageLandArea pageLandArea = testRandomLandArea();
        pageLandArea.setUserId(userId);
        pageLandArea.setIndex(index);
        return pageLandArea;
    }

    public PageLandArea testRandomLandArea() {
        int pageSize = 4;
        PageLandArea pageLandArea = new PageLandArea();
        List<LandArea> landAreaList = new ArrayList<>(pageSize);
        pageLandArea.setLandAreaList(landAreaList);
        List<LandAreaType> landAreaPool = gameProperties.getLandAreaConfig().getLandAreaPools().get(0);
        List<LandAreaType> landAreaPoolToUse = new ArrayList<>(pageSize);
        //火山
//        for (LandAreaType type: landAreaPool) {
//            if (type.getSubType() == LandAreaSubTypeValue.VOLCANO_BLOCK.getValue()) {
//                landAreaPoolToUse.add(type);
//                break;
//            }
//        }
        //水潭
//        for (LandAreaType type: landAreaPool) {
//            if (type.getSubType() == LandAreaSubTypeValue.SWAG_BLOCK.getValue()) {
//                landAreaPoolToUse.add(type);
//                break;
//            }
//        }
        //藤蔓
//        for (LandAreaType type: landAreaPool) {
//            if (type.getSubType() == LandAreaSubTypeValue.PLANT_BLOCK.getValue()) {
//                landAreaPoolToUse.add(type);
//                break;
//            }
//        }
        //老鼠
//        for (LandAreaType type: landAreaPool) {
//            if (type.getSubType() == LandAreaSubTypeValue.RAT_BLOCK.getValue()) {
//                landAreaPoolToUse.add(type);
//                break;
//            }
//        }
        //宝箱
        for (LandAreaType type: landAreaPool) {
            if (type.getSubType() == LandAreaSubTypeValue.MAGIC_BOX_BLOCK.getValue()) {
                landAreaPoolToUse.add(type);
                landAreaPoolToUse.add(type);
                landAreaPoolToUse.add(type);
                break;
            }
        }
        for (LandAreaType type: landAreaPool) {
            if (type.getSubType() == LandAreaSubTypeValue.SOIL_LAND_AREA.getValue()) {
                landAreaPoolToUse.add(type);
                if (landAreaPoolToUse.size() == pageSize) {
                    break;
                }
            }
        }
        for (int i=0; i<landAreaPoolToUse.size(); i++) {
            LandArea landArea = new LandArea();
            landArea.setIndex(i);
            landArea.setLandAreaType(landAreaPoolToUse.get(i));
            landAreaList.add(landArea);
        }
        return pageLandArea;
    }

    @Override
    public PageLandArea queryUserCurrentPageLandArea(User user, long now) throws BusinessException {
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        DigRecord currentDigRecord = userGameData.getDigRecord();
        //在挂机中
        if (currentDigRecord != null && currentDigRecord.getEndTime() == 0) {
            LandArea currentLandArea = userGameData.getCurrentLandArea();
            //当前挂机地块已结束
            if (currentLandArea.getEndTime() < now) {
                synchronizedPageLandArea(user, now);
                userGameData = userGameDataService.queryUserGameData(user);
            }
        }
        return userGameData.getCurrentPageLandArea();
    }

    @Override
    public PageLandArea queryUserPageLandAreaByIndex(User user, int index) throws BusinessException {
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        if (userGameData.getCurrentPageLandArea().getIndex() == index) {
            return userGameData.getCurrentPageLandArea();
        }
        PageLandAreaDao.PageLandAreaParam param = new PageLandAreaDao.PageLandAreaParam();
        param.setUserId(user.getId());
        param.setIndex(index);
        List<PageLandArea> pageLandAreaList = queryByParam(param);
        if (pageLandAreaList.size() > 1) {
            logger.warn("user: {} have duplicate [PageLandArea], index: {}", user.getId(), index);
        }
        return pageLandAreaList.size() > 0 ? pageLandAreaList.get(0) : null;
    }

    @Override
    public PageLandArea randomLandArea(int size, int count) throws BusinessException {
        List<LandAreaType> landAreaPool;
        if (count <= 0) {
            landAreaPool = gameProperties.getLandAreaConfig().getLandAreaPools().get(0);
        } else if (count <= 3) {
            landAreaPool = gameProperties.getLandAreaConfig().getLandAreaPools().get(1);
        } else if (count <= 6) {
            landAreaPool = gameProperties.getLandAreaConfig().getLandAreaPools().get(2);
        } else if (count <= 9) {
            landAreaPool = gameProperties.getLandAreaConfig().getLandAreaPools().get(3);
        } else {
            landAreaPool = gameProperties.getLandAreaConfig().getLandAreaPools().get(4);
        }

        List<LandArea> landAreaList = new ArrayList<>(size);
        List<LandAreaType> randomLandAreaTypeList = randomLandAreaFromPool(landAreaPool, size);
        for (int i = 0; i < size; i++) {
            LandArea landArea = new LandArea();
            landArea.setIndex(i);
            landArea.setLandAreaType(randomLandAreaTypeList.get(i));
            landAreaList.add(landArea);
        }
        PageLandArea pageLandArea = new PageLandArea();
        pageLandArea.setLandAreaList(landAreaList);
        return pageLandArea;
    }

    /**
     * 从数组池子里面随机抽取30个，
     */
    public static List<LandAreaType> randomLandAreaFromPool(List<LandAreaType> srcList, int size) throws BusinessException {
        if (srcList.size() < size) {
            logger.error("系统配置的地块数量小于地块需要随机的数量: actual size: {}, require: {}", srcList.size(), size);
            throw new BusinessException(GAME_SERVER_INTERNAL_EXCEPTION.msg, null, GAME_SERVER_INTERNAL_EXCEPTION.code);
        }
        List<LandAreaType> dest = new ArrayList<>(srcList);
        for (int i = 0; i < srcList.size(); i++) {
            int idx1 = AlgorithmUtil.randomInt(0, srcList.size());
            int idx2 = AlgorithmUtil.randomInt(0, srcList.size());
            LandAreaType temp = dest.get(idx1);
            dest.set(idx1, dest.get(idx2));
            dest.set(idx2, temp);
        }
        return dest.subList(0, size);
    }

    @Override
    public void updatePageLandArea(PageLandArea pageLandArea) {
        pageLandAreaDao.update(pageLandArea);
    }

    @Override
    public void removeUserPageLandArea(User user) {
        pageLandAreaDao.removeByUserId(user.getId());
    }

    @Override
    public BaseDao<PageLandArea> getBaseDao() {
        return pageLandAreaDao;
    }
}
