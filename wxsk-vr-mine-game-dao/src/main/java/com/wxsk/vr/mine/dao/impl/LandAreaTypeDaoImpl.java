package com.wxsk.vr.mine.dao.impl;

import com.wxsk.vr.mine.dao.LandAreaTypeDao;
import com.wxsk.vr.mine.model.LandAreaType;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class LandAreaTypeDaoImpl extends BaseDaoImpl<LandAreaType> implements LandAreaTypeDao {

    @Override
    public Update buildUpdate(LandAreaType landAreaType) {
        Update update = new Update();
        return update;
    }
}
