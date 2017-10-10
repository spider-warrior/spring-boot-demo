package com.wxsk.vr.mine.dao.impl;

import com.wxsk.vr.mine.dao.DigRecordDao;
import com.wxsk.vr.mine.model.DigRecord;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class DigRecordDaoImpl extends BaseDaoImpl<DigRecord> implements DigRecordDao {

    @Override
    public void removeDigRecordByUserId(long userId) {
        Query query = new Query();
        query.addCriteria(new Criteria("userId").is(userId));
        removeByQuery(query);
    }

    @Override
    public Update buildUpdate(DigRecord digRecord) {
        Update update = new Update();
        return update;
    }
}
