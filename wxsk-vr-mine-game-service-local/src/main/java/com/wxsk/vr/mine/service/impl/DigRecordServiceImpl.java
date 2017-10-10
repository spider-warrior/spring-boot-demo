package com.wxsk.vr.mine.service.impl;

import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.dao.BaseDao;
import com.wxsk.vr.mine.dao.DigRecordDao;
import com.wxsk.vr.mine.model.DigRecord;
import com.wxsk.vr.mine.service.DigRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 挖掘服务实现
 */
@Service
public class DigRecordServiceImpl extends BaseServiceImpl<DigRecord> implements DigRecordService {

    @Autowired
    private DigRecordDao digRecordDao;

    @Override
    public void removeUserDigRecord(User user) {
        digRecordDao.removeDigRecordByUserId(user.getId());
    }

    @Override
    public BaseDao<DigRecord> getBaseDao() {
        return digRecordDao;
    }
}
