package com.wxsk.vr.mine.service.impl;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.model.LandAreaType;
import com.wxsk.vr.mine.service.LandAreaService;
import com.wxsk.vr.mine.service.VrCoinService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LandAreaServiceImpl extends BaseServiceImpl<LandArea> implements LandAreaService {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private VrCoinService vrCoinService;

    @Override
    public int getLandAreaRequireEnergy(LandArea landArea) {
        return landArea.getLandAreaType().getConsumeEnergy() - landArea.getContainEnergy();
    }

    @Override
    public void updateLandArea(LandArea landArea) {
        Query query = new Query();
        query.addCriteria(new Criteria("_id").is(landArea.getId()));
        Update update = Update.update("landAreaType", landArea.getLandAreaType());
        update.set("endTime", landArea.getEndTime());
        update.set("containEnergy", landArea.getContainEnergy());
        update.set("informed", landArea.isInformed());
        updateFirst(query, update);
    }

    @Override
    public byte getLandAreaStatus(LandArea landArea) {
        long endTime = landArea.getEndTime();
        long now = AppContext.getCurrentRequestTimePoint().getTime();
        //超出本次挖掘的地块和为在挖掘时间内的地块
        if (endTime == 0 || landArea.getStartTime() > now) {
            //40%以内
            return 1;
        }
        //总消耗时间
        long totalMills = TimeUnit.SECONDS.toMillis(landArea.getLandAreaType().getSpendTimeInSecond());
        //不消耗时间的直接返回完成
        if (totalMills == 0) {
            return 4;
        }
        int requiredEnergy = getLandAreaRequireEnergy(landArea);
        //不需要体力且已完成
        if (requiredEnergy <= 0 && now > endTime) {
            return 4;
        }
        else {
            double ratio = ((double) landArea.getContainEnergy()) / landArea.getLandAreaType().getConsumeEnergy();
            long remainMills = endTime - now;
            //减去未挖掘进行挖掘时间的百分比
            if (remainMills > 0) {
                ratio -= (((double)remainMills)/totalMills);
            }
            if (ratio < 0.4) {
                return 1;
            }
            else if (ratio < 0.7) {
                return 2;
            }
            else {
                return 3;
            }
        }
    }

    @Override
    public void generateVrCoinAward(final LandArea landArea) throws BusinessException {
        try {
            LandAreaType cloneLandAreaType = (LandAreaType)landArea.getLandAreaType().clone();
            AwardType cloneAwardType = (AwardType)landArea.getLandAreaType().getAwardType().clone();
            cloneLandAreaType.setAwardType(cloneAwardType);
            landArea.setLandAreaType(cloneLandAreaType);
            int vrAward = vrCoinService.calculateAwardVrCoin(cloneAwardType.getLevel());
            cloneAwardType.setAmount(vrAward);
            landArea.setLandAreaType(cloneLandAreaType);
        } catch (CloneNotSupportedException e) {
            logger.error("克隆[LandAreaType]实例失败", e);
        }
    }

}
