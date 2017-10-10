package com.wxsk.vr.mine.service;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.model.PageLandArea;

/**
 * 页面地块服务
 */
public interface PageLandAreaService extends BaseService<PageLandArea> {

    /**
     * 初始化用户地块数据
     */
    PageLandArea generateUserPageLandArea(Long userId, int index) throws BusinessException;

    /**
     * 查询用户当前页地块
     */
    PageLandArea queryUserCurrentPageLandArea(User user, long now) throws BusinessException;

    /**
     * 同步地块数据
     */
    void synchronizedPageLandArea(User user, long now) throws BusinessException;

    /**
     * 根据页码查询用户地块
     *
     * @param user 用户
     * @param index  页面下标
     */
    PageLandArea queryUserPageLandAreaByIndex(User user, int index) throws BusinessException;

    /**
     * 随机地块
     *
     * @param size  list size
     * @param count 刷新次数
     */
    PageLandArea randomLandArea(int size, int count) throws BusinessException;

    /**
     * 更新PageLandArea
     */
    void updatePageLandArea(PageLandArea pageLandArea);

    /**
     * 清理用户PageLandArea
     */
    void removeUserPageLandArea(User user);

}
