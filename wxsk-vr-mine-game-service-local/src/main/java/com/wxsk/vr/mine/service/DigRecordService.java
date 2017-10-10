package com.wxsk.vr.mine.service;

import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.model.DigRecord;

/**
 * 挖掘记录服务
 */
public interface DigRecordService extends BaseService<DigRecord> {

    /**
     * 清理用户挖掘记录
     */
    void removeUserDigRecord(User user);

}
