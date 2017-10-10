package com.wxsk.vr.mine.dao.impl;

import com.wxsk.vr.mine.dao.LandAreaDao;
import com.wxsk.vr.mine.model.LandArea;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class LandAreaDaoImpl extends BaseDaoImpl<LandArea> implements LandAreaDao {

    @Override
    public Update buildUpdate(LandArea landArea) {
        Update update = new Update();
        return update;
    }
}
