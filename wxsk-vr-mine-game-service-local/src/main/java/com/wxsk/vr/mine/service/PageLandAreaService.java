package com.wxsk.vr.mine.service;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.model.PageLandArea;

/**
 * 页面地块服务
 */
public interface PageLandAreaService extends BaseService<PageLandArea> {

    /**
     * 随机地块
     * @param size list size
     * @param count 刷新次数
     * */
    PageLandArea randomLandArea(int size, int count);

    /**
     * 查询当前矿区状态
     * @param user 用户数据
     * */
    PageLandArea queryCurrentLandArea(User user) throws BusinessException;

    /**
     * 查询下一矿区状态
     * @param user 用户数据
     * */
    PageLandArea queryNextLandArea(User user);

    /**
     * 计算矿区占用的体力(已消耗的体力)
     * @param pageLandArea 矿区
     * @return 体力
     * */
    Integer sumContainEnergyOfPageLandArea(PageLandArea pageLandArea);
    

    /**
     * 矿区内以挂机的最后一块的结束时间
     * @param pageLandArea
     * @return 
     */
    Long lastTimeOfPageLandArea(PageLandArea pageLandArea);

    /**
     * 根据页码查询用户地块
     * @param userId 用户Id
     * @param index 页面下标
     * */
    PageLandArea queryUserPageLandAreaByIndex(long userId, int index);

    /**
     * 更新PageLandArea
     * */
    void updatePageLandArea(PageLandArea pageLandArea);

    /**
     * 清理用户PageLandArea
     * */
    void removeUserPageLandArea(User user);

    /**
     * 检查地块是否已开始挖掘
     * */
    boolean isStarted(PageLandArea pageLandArea, long nowTime);

}
