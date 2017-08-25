package com.wxsk.vr.mine.controller.response.wrapper;

import com.wxsk.vr.mine.common.constant.enums.LandAwardType;
import com.wxsk.vr.mine.controller.response.vo.LandAreaHarvestVo;
import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.LandArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * LandAreaHarvestVo响应包装类
 */
@Service
public class LandAreaHarvestVoWrapper {

    private static final Logger logger = LogManager.getLogger(LandAreaHarvestVoWrapper.class);

    public List<LandAreaHarvestVo> buildLandAreaAward(List<LandArea> landAreas) {
        List<LandAreaHarvestVo> landAreaHarvestVoList = new ArrayList<>();
        if (landAreas != null) {
            Map<Byte, Integer> harvestMap = new HashMap<>();
            for (LandArea landArea: landAreas) {
                AwardType awardType = landArea.getLandAreaType().getAwardType();
                Integer saved = harvestMap.get(awardType.getValue());
                if (saved == null) {
                    saved = 0;
                }
                if (awardType != null && awardType.getValue() != 0) {
                    saved += awardType.getAmount();
                    harvestMap.put(awardType.getValue(), saved);
                }
            }
            for (Map.Entry<Byte, Integer> entry: harvestMap.entrySet()) {
                LandAreaHarvestVo areaHarvestVo = new LandAreaHarvestVo();
                areaHarvestVo.setType(entry.getKey());
                areaHarvestVo.setName(LandAwardType.getLandAwardType(entry.getKey()).name);
                //无限币
                if (entry.getKey() == LandAwardType.VR_COIN.value) {
                    BigDecimal bd = new BigDecimal(entry.getValue());
                    areaHarvestVo.setAmount(bd.divide(new BigDecimal(10), 1, RoundingMode.DOWN).doubleValue());
                }
                else {
                    areaHarvestVo.setAmount(entry.getValue().doubleValue());
                }
                landAreaHarvestVoList.add(areaHarvestVo);
            }
        }
        if (landAreaHarvestVoList.size() > 0) {
            logger.info("harvests: {}", landAreaHarvestVoList);
        }
        return landAreaHarvestVoList;
    }


    public List<LandAreaHarvestVo> buildLandAreaAward(Map<Byte, AwardType> landAreaAward) {
        if (landAreaAward == null) {
            return Collections.emptyList();
        }
        List<LandAreaHarvestVo> landAreaHarvestVoList = new ArrayList<>();
        for (AwardType awardType: landAreaAward.values()) {
            if (awardType.getValue() != 0) {
                LandAreaHarvestVo areaHarvestVo = new LandAreaHarvestVo();
                areaHarvestVo.setType(awardType.getValue());
                LandAwardType landAwardType = LandAwardType.getLandAwardType(awardType.getValue());
                if (landAwardType!= null) {
                    areaHarvestVo.setName(landAwardType.name);
                }
                else {
                    areaHarvestVo.setName(awardType.getName());
                }
                //无限币
                if (awardType.getValue() == LandAwardType.VR_COIN.value) {
                    BigDecimal bd = new BigDecimal(awardType.getAmount());
                    areaHarvestVo.setAmount(bd.divide(new BigDecimal(10), 1, RoundingMode.DOWN).doubleValue());
                }
                else {
                    areaHarvestVo.setAmount((double)awardType.getAmount());
                }
                landAreaHarvestVoList.add(areaHarvestVo);
            }
        }
        if (landAreaHarvestVoList.size() > 0) {
            logger.info("harvests: {}", landAreaHarvestVoList);
        }
        return landAreaHarvestVoList;
    }
}


