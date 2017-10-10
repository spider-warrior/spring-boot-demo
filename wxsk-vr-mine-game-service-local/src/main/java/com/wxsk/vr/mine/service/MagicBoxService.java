package com.wxsk.vr.mine.service;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.constant.enums.ConsumeType;
import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.MagicBox;

import java.util.Collection;
/**
 * 宝箱服务
 */
public interface MagicBoxService extends BaseService<MagicBox> {

    /**
     * 开宝箱
     * @param user 用户
     * @param type 开箱方式
     * @param amount 打开宝箱数量
     * @param now 当前时间
     *
     * @return 开箱收益
     */
    Collection<AwardType> consumeMagicBox(User user, ConsumeType type, byte amount, long now) throws BusinessException;

    /**
     * 更新宝箱
     * */
    void updateMagicBox(MagicBox magicBox);

    /**
     * 清理用户宝箱
     * */
    void removeUserMagicBox(User user);

    /**
     * 计算用户未使用的宝箱数量
     * */
    long countUserUnusedMagicBox(User user);
}
