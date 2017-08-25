package com.wxsk.vr.mine.controller.response.wrapper;

import com.wxsk.vr.mine.common.util.DateUtil;
import com.wxsk.vr.mine.controller.response.vo.LandAreaVo;
import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.DigRecord;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.model.UserGameData;
import com.wxsk.vr.mine.service.LandAreaService;
import com.wxsk.vr.mine.service.UserLandAreaService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * LandAreaVo包装类
 */
@Service
public class LandAreaVoWrapper {

    private static final Logger logger = LogManager.getLogger(LandAreaVoWrapper.class);

    @Autowired
    private UserLandAreaService userLandAreaService;
    @Autowired
    private LandAreaService landAreaService;

    public LandAreaVo buildLandAreaVo(UserGameData userGameData, LandArea landArea) {
        if (landArea == null) {
            return null;
        }
        LandAreaVo landAreaVo = new LandAreaVo();
        landAreaVo.setName(landArea.getLandAreaType().getName());
        landAreaVo.setType(landArea.getLandAreaType().getType());
        landAreaVo.setSubType(landArea.getLandAreaType().getSubType());
        landAreaVo.setIndex(landArea.getIndex());
        landAreaVo.setAwardName(landArea.getLandAreaType().getAwardType().getName());
        landAreaVo.setInformed(landArea.isInformed());
        AwardType awardType = landArea.getLandAreaType().getAwardType();
        if (awardType != null) {
            landAreaVo.setAwardType(awardType.getValue());
            //无限币
            if (awardType.getValue() == 3) {
                BigDecimal bd = new BigDecimal(awardType.getAmount());
                landAreaVo.setAwardAmount(bd.divide(new BigDecimal(10), 1, RoundingMode.DOWN).doubleValue());

            }
            else {
                landAreaVo.setAwardAmount((double)awardType.getAmount());
            }
        }
        landAreaVo.setConsumeEnergy(landArea.getLandAreaType().getConsumeEnergy());
        landAreaVo.setContainedEnergy(landArea.getContainEnergy());
        landAreaVo.setConsumeTimeInSecond(landArea.getLandAreaType().getSpendTimeInSecond());
        landAreaVo.setConsumeTime(DateUtil.getDistance(landArea.getLandAreaType().getSpendTimeInSecond() * 1000L));
        DigRecord digRecord = userGameData.getDigRecord();
        if (digRecord == null) {
            landAreaVo.setIng(false);
        }
        else {
            landAreaVo.setIng(userLandAreaService.isWorkerOn(System.currentTimeMillis(), digRecord.getEndTime(), landArea));
        }
        if (landArea.getEndTime() != 0) {
            landAreaVo.setEndTime(new Date(landArea.getEndTime()));
        }
        landAreaVo.setStatus(landAreaService.getLandAreaStatus(landArea));
        return landAreaVo;
    }
}
