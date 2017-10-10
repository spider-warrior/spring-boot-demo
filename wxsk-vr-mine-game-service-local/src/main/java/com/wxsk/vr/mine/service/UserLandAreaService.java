package com.wxsk.vr.mine.service;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.model.*;

import java.util.List;
import java.util.Map;

/**
 * 用户地块服务
 */
public interface UserLandAreaService extends BaseService<UserLandArea>{

    /**
     * 查询用户地块信息
     * @param user 用户
     * */
    UserLandArea queryUserLandArea(User user) throws BusinessException;

    /**
     * 初始化用户地块数据
     * @param user 用户数据
     * */
    void initUserLandArea(User user) throws BusinessException;

    /**
     * 更系用户地块数据
     * @param userLandArea 用户地块数据
     * */
    void updateUserLandArea(UserLandArea userLandArea);

    /**
     * 消耗体力挖地
     * @param landArea 地块
     * @param energy 可用能量
     * @param timeMark 开始时间计算节点
     *
     * @return 是否处理过
     * */
    boolean consumeEnergyOnLandArea(LandArea landArea, int energy, long timeMark) throws BusinessException;

    /**
     * 消耗体力挖一堆地
     * @param energy 可用能量
     * @param timeMark 计算开始时间
     * @param pageLandAreas 可挖掘地块
     *
     * @return 挖掘记录
     * */
	DigRecord consumeEnergyOnLandAreaList(int energy, long timeMark, final PageLandArea... pageLandAreas) throws BusinessException;

    /**
     * 查询用户本次挖掘未通知的收益
     * @param user 用户数据
     * */
    Map<Byte, AwardType> queryUserUninformedLandAreaAward(User user) throws BusinessException;

    /**
     * 查询用户本次挖掘已通知的收益
     * @param user 用户数据
     * */
    Map<Byte, AwardType> queryUserInformedLandAreaAward(User user) throws BusinessException;

    /**
     * 过滤时间段内的地块
     * */
    List<LandArea> filterUserLandAreaByTimeline(PageLandArea pageLandArea, long startTime, long endTime) throws BusinessException;

    /**
     * 查询当前矿区地块详细信息
     * */
    LandArea queryCurrentLandAreaByIndex(User user, int index) throws BusinessException;

    /**
     * 查询下一矿区地块详细信息
     * */
    LandArea queryNextLandAreaByIndex(User user, int index) throws BusinessException;

    /**
     * 翻页
     * @param user 用户数据
     * */
    void turnToNextPage(User user) throws BusinessException;

    /**
     * 查询矿工位置
     * @param user 用户数据
     * @return 矿工位置, -1 未找到矿工位置
     * */
    int queryWorkerIndex(User user) throws BusinessException;

    /**
     * 查询矿工位置
     * @return 矿工位置, -1 未找到矿工位置
     * */
    int queryWorkerIndex(PageLandArea pageLandArea, long endTime);

    /**
     * 小人是否在石块上
     * @param landArea 地块信息
     * @param now 当前时间
     * @param endTime 挖矿结束时间
     * @return true 小人在该石块上 otherwise false
     * */
    boolean isWorkerOn(long now, long endTime, LandArea landArea);

}
