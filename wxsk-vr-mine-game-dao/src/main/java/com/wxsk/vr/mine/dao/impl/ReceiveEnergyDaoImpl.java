package com.wxsk.vr.mine.dao.impl;

import com.wxsk.vr.mine.dao.ReceiveEnergyDao;
import com.wxsk.vr.mine.model.ReceiveEnergy;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class ReceiveEnergyDaoImpl extends BaseDaoImpl<ReceiveEnergy> implements ReceiveEnergyDao {

    @Override
    public Update buildUpdate(ReceiveEnergy receiveEnergy) {
        Update update = new Update();
        return update;
    }
}
