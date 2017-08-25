package com.wxsk.vr.mine.service;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.vr.mine.model.LandArea;

/**
 * 地块服务
 */
public interface LandAreaService extends BaseService<LandArea> {

    /**
     * 获取每个地块缩消耗体力
     * 单位: 单位体力
     * @param landArea 地块
     * */
    int getLandAreaRequireEnergy(LandArea landArea);

    /**
     * 更新地块
     * @param landArea 地块数据
     * */
    void updateLandArea(LandArea landArea);

    /**
     * 获取石块状态
     * 石块状态:
     * 1: 100 - 61 ---> 40%
     * 2: 60 - 31  ---> 70%
     * 3: 30 - 0   ---> 100%
     * @return 石块状态
     * */
    byte getLandAreaStatus(LandArea landArea);

    /**
     * 生成无限币奖励
     * @param landArea 地块数据
     * */
    void generateVrCoinAward(final LandArea landArea) throws BusinessException;

}
