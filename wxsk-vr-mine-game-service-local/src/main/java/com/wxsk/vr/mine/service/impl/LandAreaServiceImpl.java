package com.wxsk.vr.mine.service.impl;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.common.exception.ServiceException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.constant.enums.AffectType;
import com.wxsk.vr.mine.common.constant.enums.GameBuff;
import com.wxsk.vr.mine.common.constant.enums.LandAreaAwardTypeValue;
import com.wxsk.vr.mine.dao.BaseDao;
import com.wxsk.vr.mine.dao.LandAreaDao;
import com.wxsk.vr.mine.model.*;
import com.wxsk.vr.mine.service.DigRecordService;
import com.wxsk.vr.mine.service.LandAreaService;
import com.wxsk.vr.mine.service.UserGameDataService;
import com.wxsk.vr.mine.service.VrCoinService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.wxsk.vr.mine.common.constant.enums.ServiceErrorCode.*;

@Service
public class LandAreaServiceImpl extends BaseServiceImpl<LandArea> implements LandAreaService {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private LandAreaDao landAreaLandAreaDao;
    @Autowired
    private UserGameDataService userGameDataService;
    @Autowired
    private LandAreaService landAreaService;
    @Autowired
    private VrCoinService vrCoinService;
    @Autowired
    private DigRecordService digRecordService;

    @Override
    public LandArea startOfflineDigging(User user, long now) throws ServiceException, BusinessException {
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        DigRecord digRecord = userGameData.getDigRecord();
        if (digRecord != null && digRecord.getEndTime() == 0) {
            throw new BusinessException(GAME_ALREADY_IN_MINING.msg, null, GAME_ALREADY_IN_MINING.code);
        }
        PageLandArea currentPageLandArea = userGameData.getCurrentPageLandArea();
        if (currentPageLandArea == null) {
            logger.error("服务器数据异常: user: {}, PageLandArea missing, page index: {}", user.getId(), currentPageLandArea);
            throw new BusinessException(GAME_SERVER_INTERNAL_EXCEPTION.msg, null, GAME_SERVER_INTERNAL_EXCEPTION.code);
        }
        //兑换体力
        userGameDataService.exchangeTimeToEnergy(userGameData, now);
        List<LandArea> currentLandAreaList = currentPageLandArea.getLandAreaList();
        LandArea currentLandArea = userGameData.getCurrentLandArea();
        //如果未曾挂机或者挂机到最后一个地块
        if (currentLandArea == null || currentLandArea.getIndex() == currentLandAreaList.size() - 1) {
            currentLandArea = currentLandAreaList.get(0);
        }
        else {
            currentLandArea = currentLandAreaList.get(currentLandArea.getIndex() + 1);
        }
        int requiredEnergy = getLandAreaRequireEnergy(userGameData, currentLandArea);
        //@throws: 体力不足
        if (requiredEnergy > userGameData.getEnergy()) {
            throw new BusinessException(GAME_ENERGY_NOT_ENOUGH.msg, null, GAME_ENERGY_NOT_ENOUGH.code);
        }
        //持久化旧记录
        if (digRecord != null) {
            digRecordService.insert(digRecord);
        }
        consumeEnergyOnLandArea(userGameData, currentLandArea, userGameData.getEnergy(), now);
        //更新PageLandArea
        DigRecord newDigRecord = new DigRecord();
        newDigRecord.setUserId(user.getId());
        newDigRecord.setLandAreaStartIndex(currentLandArea.getIndex());
        newDigRecord.addPageIndex(currentPageLandArea.getIndex());
        newDigRecord.setStartTime(now);
        newDigRecord.setInformed(false);
        newDigRecord.setRemainEnergy(userGameData.getEnergy());
        userGameData.setEnergy(userGameData.getEnergy() - requiredEnergy);
        userGameData.setDigRecord(newDigRecord);
        userGameData.setLastConsumeEnergyTime(now);
        userGameData.setCurrentLandArea(currentLandArea);
        //更新UserGameData#lastConsumeEnergyTime,UserGameData#predictEnergyFullTime
        userGameDataService.exchangeTimeToEnergy(userGameData, now);
        //更新UserGameData
        userGameDataService.updateUserGameData(userGameData);
        return currentLandArea;
    }


    @Override
    public void consumeEnergyOnLandArea(UserGameData userGameData, LandArea landArea, int energy, long timeMark) throws BusinessException {
        int requireEnergy = getLandAreaRequireEnergy(userGameData, landArea);
        int energyToUse = requireEnergy > energy ? energy : requireEnergy;
        landArea.setContainEnergy(energyToUse);
        landArea.setStartTime(timeMark);
        landArea.setEndTime(timeMark + getLandAreaRequireTime(userGameData, landArea));
        //无限币类型
        if (landArea.getLandAreaType().getAwardType().getValue() == LandAreaAwardTypeValue.VR_COIN.value) {
            landAreaService.generateVrCoinAward(landArea, timeMark);
        }
        GameBuff gameBuff = GameBuff.getGameBuff(landArea.getLandAreaType().getSubType());
        //火山buff不能自动触发
        if (GameBuff.VOLCANO_BUFF != gameBuff) {
            userGameDataService.addGameBuff(userGameData, gameBuff);
        }
    }

    @Override
    public int getLandAreaRequireEnergy(UserGameData userGameData, LandArea landArea) {
        int requiredEnergy = landArea.getLandAreaType().getConsumeEnergy() - landArea.getContainEnergy();
        List<GameBuff> gameBuffList = userGameData.getBuffs();
        if (gameBuffList != null && !gameBuffList.isEmpty()) {
            for (GameBuff buff: gameBuffList) {
                //体力buff
                if (buff.getAffectType() == AffectType.ENERGY) {
                    requiredEnergy*=buff.getCoefficient();
                }
            }
        }
        return requiredEnergy;
    }

    @Override
    public long getLandAreaRequireTime(UserGameData userGameData, LandArea landArea) {
        long requiredTime = TimeUnit.SECONDS.toMillis(landArea.getLandAreaType().getSpendTimeInSecond());
        List<GameBuff> gameBuffList = userGameData.getBuffs();
        if (gameBuffList != null && !gameBuffList.isEmpty()) {
            for (GameBuff buff: gameBuffList) {
                //体力buff
                if (buff.getAffectType() == AffectType.TIME) {
                    requiredTime*=buff.getCoefficient();
                }
            }
        }
        return requiredTime;
    }

    @Override
    public void generateVrCoinAward(final LandArea landArea, long now) throws BusinessException {
        try {
            LandAreaType cloneLandAreaType = (LandAreaType) landArea.getLandAreaType().clone();
            AwardType cloneAwardType = (AwardType) landArea.getLandAreaType().getAwardType().clone();
            cloneLandAreaType.setAwardType(cloneAwardType);
            landArea.setLandAreaType(cloneLandAreaType);
            int vrAward = vrCoinService.calculateAwardVrCoin(cloneAwardType.getLevel(), now);
            cloneAwardType.setAmount(vrAward);
            landArea.setLandAreaType(cloneLandAreaType);
        } catch (CloneNotSupportedException e) {
            logger.error("克隆[LandAreaType]实例失败", e);
        }
    }

    @Override
    public byte getLandAreaStatus(LandArea landArea, long now) {
        long startTime = landArea.getStartTime();
        //未挖掘
        if (startTime == 0) {
            return 1;
        }
        long endTime = landArea.getEndTime();
        //已结束
        if (endTime <= now) {
            return 5;
        }
        //总消耗时间
        long totalMills = endTime - startTime;
        long passedTime = now - startTime;
        double ratio = (((double)passedTime) / totalMills);
        if (ratio <= 0.333) {
            return 2;
        } else if (ratio <= 0.666) {
            return 3;
        } else {
            return 4;
        }
    }

    @Override
    public BaseDao<LandArea> getBaseDao() {
        return landAreaLandAreaDao;
    }
}
