package com.wxsk.vr.mine.service;

import org.springframework.stereotype.Service;

/**
 * 无限币服务
 */
@Service
public interface VrCoinService {

    /**
     * 获取VR币上限
     */
    int getMaxVrCoin();

    /**
     * 计算VR币奖励
     *
     * @param level VR币子类型
     */
    int calculateAwardVrCoin(int level, long now);

    /**
     * 计算VR币上线天数
     */
    long calculateOnlineDayCount(long now);

    /**
     * 获取VR币掉率
     */
    double calculateVRCoinRate(long now);

}
