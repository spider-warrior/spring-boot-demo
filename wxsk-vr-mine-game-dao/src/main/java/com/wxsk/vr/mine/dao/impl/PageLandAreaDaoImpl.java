package com.wxsk.vr.mine.dao.impl;

import com.wxsk.vr.mine.dao.PageLandAreaDao;
import com.wxsk.vr.mine.model.PageLandArea;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class PageLandAreaDaoImpl extends BaseDaoImpl<PageLandArea> implements PageLandAreaDao {

    @Override
    public void removeByUserId(long userId) {
        Query query = new Query();
        query.addCriteria(new Criteria("userId").is(userId));
        removeByQuery(query);
    }

    @Override
    public Update buildUpdate(PageLandArea pageLandArea) {
        Update update = Update.update("userId", pageLandArea.getUserId());
        update.set("landAreaList", pageLandArea.getLandAreaList());
        return update;
    }
}
