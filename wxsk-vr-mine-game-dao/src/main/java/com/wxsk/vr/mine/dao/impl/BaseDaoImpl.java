package com.wxsk.vr.mine.dao.impl;

import com.mongodb.WriteResult;
import com.wxsk.vr.mine.common.util.AppReflectUtil;
import com.wxsk.vr.mine.dao.BaseDao;
import com.wxsk.vr.mine.model.BaseModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public abstract class BaseDaoImpl<T  extends BaseModel> implements BaseDao<T> {

    protected final Class<T> clazz;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public void insert(T t) {
        mongoTemplate.insert(t);
    }

    @Override
    public WriteResult update(T t) {
        Query query = new Query();
        query.addCriteria(new Criteria("_id").is(t.getId()).and("version").is(t.getVersion()));
        Update update = buildUpdate(t);
        return mongoTemplate.updateFirst(query, update, clazz);
    }

    @Override
    public WriteResult remove(T t) {
        return mongoTemplate.remove(t);
    }

    @Override
    public WriteResult removeByQuery(Query query) {
        return mongoTemplate.remove(query, clazz);
    }

    @Override
    public WriteResult updateFirst(Query query, Update update) {
        return mongoTemplate.updateFirst(query, update, clazz);
    }

    @Override
    public T queryById(ObjectId id) {
        return mongoTemplate.findById(id, clazz);
    }

    @Override
    public long countByParam(BaseParam baseParam) {
        return mongoTemplate.count(baseParam.buildCriteriaQuery(), clazz);
    }

    @Override
    public List<T> queryByParam(BaseParam baseParam) {
        return queryByNativeQuery(baseParam.buildQuery());
    }

    @Override
    public List<T> queryByNativeQuery(Query query) {
        return mongoTemplate.find(query, clazz);
    }

    @Override
    public WriteResult deleteByParam(BaseParam baseParam) {
        return mongoTemplate.remove(baseParam.buildQuery(), clazz);
    }

    public BaseDaoImpl() {
        clazz = AppReflectUtil.findTypeParam(this, BaseDaoImpl.class, "T");
    }

    public abstract Update buildUpdate(T t);
}
