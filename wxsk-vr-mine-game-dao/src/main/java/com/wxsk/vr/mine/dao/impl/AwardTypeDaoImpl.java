package com.wxsk.vr.mine.dao.impl;

import com.wxsk.vr.mine.dao.AwardTypeDao;
import com.wxsk.vr.mine.model.AwardType;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class AwardTypeDaoImpl extends BaseDaoImpl<AwardType> implements AwardTypeDao {

    @Override
    public Update buildUpdate(AwardType awardType) {
        Update update = new Update();
        return update;
    }

    public AwardTypeDaoImpl() {
    }
}
