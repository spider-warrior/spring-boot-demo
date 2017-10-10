package com.wxsk.vr.mine.controller.response.wrapper;

import com.wxsk.vr.mine.common.constant.enums.LandAreaAwardTypeValue;
import com.wxsk.vr.mine.controller.response.vo.LandAreaHarvestVo;
import com.wxsk.vr.mine.controller.response.vo.constants.VoType;
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
                areaHarvestVo.setStructureType(VoType.HARVEST.getValue());
                areaHarvestVo.setType(entry.getKey());
                LandAreaAwardTypeValue landAreaAwardTypeValue = LandAreaAwardTypeValue.getLandAreaAwardTypeValue(entry.getKey());
                if (landAreaAwardTypeValue != null) {
                    areaHarvestVo.setName(landAreaAwardTypeValue.name);
                }
                //无限币
                if (entry.getKey() == LandAreaAwardTypeValue.VR_COIN.value) {
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


    public List<LandAreaHarvestVo> buildLandAreaAward(Collection<AwardType> landAreaAward) {
        if (landAreaAward == null) {
            return Collections.emptyList();
        }
        List<LandAreaHarvestVo> landAreaHarvestVoList = new ArrayList<>();
        for (AwardType awardType: landAreaAward) {
            if (awardType.getValue() != 0) {
                LandAreaHarvestVo areaHarvestVo = new LandAreaHarvestVo();
                areaHarvestVo.setStructureType(VoType.HARVEST.getValue());
                areaHarvestVo.setType(awardType.getValue());
                LandAreaAwardTypeValue landAreaAwardTypeValue = LandAreaAwardTypeValue.getLandAreaAwardTypeValue(awardType.getValue());
                if (landAreaAwardTypeValue != null) {
                    areaHarvestVo.setName(landAreaAwardTypeValue.name);
                }
                else {
                    areaHarvestVo.setName(awardType.getName());
                }
                //宝箱 || 体力 || 经验
                if(awardType.getValue() == LandAreaAwardTypeValue.MAGIC_BOX.value || awardType.getValue() == LandAreaAwardTypeValue.ENERGY.value || awardType.getValue() == LandAreaAwardTypeValue.EXP.value) {
                    areaHarvestVo.setAmount((double)awardType.getAmount());
                }
                else {
                    BigDecimal bd = new BigDecimal(awardType.getAmount());
                    areaHarvestVo.setAmount(bd.divide(new BigDecimal(10), 1, RoundingMode.DOWN).doubleValue());
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


