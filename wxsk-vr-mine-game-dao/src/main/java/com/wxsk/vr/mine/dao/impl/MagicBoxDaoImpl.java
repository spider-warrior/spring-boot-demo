package com.wxsk.vr.mine.dao.impl;

import com.wxsk.vr.mine.dao.MagicBoxDao;
import com.wxsk.vr.mine.model.MagicBox;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class MagicBoxDaoImpl extends BaseDaoImpl<MagicBox> implements MagicBoxDao {

    @Override
    public void removeByUserId(long userId) {
        Query query = new Query();
        query.addCriteria(new Criteria("userId").is(userId));
        mongoTemplate.remove(query, clazz);
    }

    @Override
    public long countMagicBoxByParam(MagicBoxParam magicBoxParam) {
        return mongoTemplate.count(magicBoxParam.buildQuery(), clazz);
    }

    @Override
    public Update buildUpdate(MagicBox magicBox) {
        Update update = Update.update("userId", magicBox.getUserId());
        update.set("createTime", magicBox.getCreateTime());
        update.set("pageLandAreaIndex", magicBox.getPageLandAreaIndex());
        update.set("landAreaIndex", magicBox.getLandAreaIndex());
        update.set("useTime", magicBox.getUseTime());
        update.set("payType", magicBox.getPayType());
        update.set("awardType", magicBox.getAwardType());
        update.set("awardCnt", magicBox.getAwardCnt());
        return update;
    }

}

