package com.wxsk.vr.mine.controller.response.wrapper;

import com.wxsk.vr.mine.common.util.DateUtil;
import com.wxsk.vr.mine.controller.response.vo.LandAreaVo;
import com.wxsk.vr.mine.controller.response.vo.constants.VoType;
import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.service.LandAreaService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * LandAreaVo包装类
 */
@Service
public class LandAreaVoWrapper {

    private static final Logger logger = LogManager.getLogger(LandAreaVoWrapper.class);
    private static final LandAreaVo empty = new LandAreaVo();

    @Autowired
    private LandAreaService landAreaService;

    public LandAreaVo buildLandAreaVo(LandArea landArea, long now) {
        if (landArea == null) {
            return null;
        }
        return buildLandAreaVo(landArea, landArea.getStartTime() < now && now < landArea.getEndTime(), landAreaService.getLandAreaStatus(landArea, now));
    }

    public LandAreaVo buildLandAreaVo(LandArea landArea, boolean ing, byte status) {
        if (landArea == null) {
            return null;
        }
        LandAreaVo landAreaVo = new LandAreaVo();
        landAreaVo.setStructureType(VoType.LAND_AREA.getValue());
        landAreaVo.setName(landArea.getLandAreaType().getName());
        landAreaVo.setType(landArea.getLandAreaType().getType());
        landAreaVo.setSubType(landArea.getLandAreaType().getSubType());
        landAreaVo.setIndex(landArea.getIndex());
        landAreaVo.setAwardName(landArea.getLandAreaType().getAwardType().getName());
        AwardType awardType = landArea.getLandAreaType().getAwardType();
        landAreaVo.setAwardType(awardType.getValue());
        BigDecimal bd;
        //已经开始或结束
        if (landArea.getStartTime() != 0) {
            landAreaVo.setConsumeEnergy(landArea.getContainEnergy());
            landAreaVo.setConsumeTimeInSecond((int)TimeUnit.MILLISECONDS.toSeconds(landArea.getEndTime() - landArea.getStartTime()));
            landAreaVo.setConsumeTime(DateUtil.getDistance(landArea.getEndTime() - landArea.getStartTime()));
            bd = new BigDecimal(landArea.getActualProfitAmount());
        }
        else {
            landAreaVo.setConsumeEnergy(landArea.getLandAreaType().getConsumeEnergy());
            landAreaVo.setConsumeTimeInSecond(landArea.getLandAreaType().getSpendTimeInSecond());
            landAreaVo.setConsumeTime(DateUtil.getDistance(TimeUnit.SECONDS.toMillis(landArea.getLandAreaType().getSpendTimeInSecond())));
            bd = new BigDecimal(awardType.getAmount());
        }
        landAreaVo.setAwardAmount(bd.divide(new BigDecimal(10), 1, RoundingMode.DOWN).doubleValue());
        landAreaVo.setContainedEnergy(landArea.getContainEnergy());
        landAreaVo.setIng(ing);
        if (landArea.getEndTime() != 0) {
            landAreaVo.setEndTime(new Date(landArea.getEndTime()));
        }
        landAreaVo.setEndTimeMills(landArea.getEndTime());
        landAreaVo.setStatus(status);
        return landAreaVo;
    }

    public List<LandAreaVo> buildLandAreaVoList(List<LandArea> landAreaList, long now) {
        List<LandAreaVo> landAreaVoList = new ArrayList<>(landAreaList.size());
        boolean hasIng = false;
        for (int i = 0; i < landAreaList.size(); i++) {
            LandArea landArea = landAreaList.get(i);
            boolean ing = (landArea.getStartTime() < now && now < landArea.getEndTime() || ((i != 0) && landAreaList.get(i - 1).getEndTime() != 0 && landAreaList.get(i - 1).getEndTime() < now && landArea.getStartTime() == 0));
            if (!hasIng && ing) {
                hasIng = true;
            }
            landAreaVoList.add(buildLandAreaVo(landArea, ing, landAreaService.getLandAreaStatus(landArea, now)));
        }
        if (!hasIng) {
            landAreaVoList.get(0).setIng(true);
        }

        return landAreaVoList;
    }
}
