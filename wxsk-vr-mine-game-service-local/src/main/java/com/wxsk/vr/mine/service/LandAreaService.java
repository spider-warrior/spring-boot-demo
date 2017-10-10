package com.wxsk.vr.mine.service;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.common.exception.ServiceException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.model.UserGameData;

/**
 * 地块服务
 */
public interface LandAreaService extends BaseService<LandArea> {

    /**
     * @param user 当前用户
     * @return 当前挖掘的地块
     * @throws ServiceException
     * @name 开启离线挂机模式
     */
    LandArea startOfflineDigging(User user, long now) throws ServiceException, BusinessException;

    /**
     * 消耗体力挖地
     *
     * @param userGameData 用户游戏数据
     * @param landArea 地块
     * @param energy   可用能量
     * @param timeMark 开始时间计算节点
     */
    void consumeEnergyOnLandArea(UserGameData userGameData, LandArea landArea, int energy, long timeMark) throws BusinessException;

    /**
     * 获取地块缩消耗体力
     * 单位: 单位体力
     *
     * @param landArea 地块
     * @param userGameData 用户游戏数据
     */
    int getLandAreaRequireEnergy(UserGameData userGameData, LandArea landArea);

    /**
     * 获取地块缩消耗时间
     * 单位: 单位体力
     *
     * @param landArea 地块
     * @param userGameData 用户游戏数据
     */
    long getLandAreaRequireTime(UserGameData userGameData, LandArea landArea);

    /**
     * 生成无限币奖励
     *
     * @param landArea 地块数据
     * @param now      当前时间
     */
    void generateVrCoinAward(final LandArea landArea, long now) throws BusinessException;

    /**
     * 获取石块状态
     * 石块状态:
     * 1: 100 - 61 ---> 40%
     * 2: 60 - 31  ---> 70%
     * 3: 30 - 0   ---> 100%
     *
     * @return 石块状态
     */
    byte getLandAreaStatus(LandArea landArea, long now);

}
