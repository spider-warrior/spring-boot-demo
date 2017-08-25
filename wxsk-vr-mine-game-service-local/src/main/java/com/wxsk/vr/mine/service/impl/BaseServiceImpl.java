package com.wxsk.vr.mine.service.impl;

import com.mongodb.WriteResult;
import com.wxsk.vr.mine.common.util.AppReflectUtil;
import com.wxsk.vr.mine.service.BaseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * 基础服务实现
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    protected final Class<T> clazz;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public void insert(T t) {
        mongoTemplate.insert(t);
    }

    @Override
    public void save(T t) {
        mongoTemplate.save(t);
    }

    @Override
    public WriteResult remove(T t) {
        return mongoTemplate.remove(t);
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
    public List<T> queryByParam(Query query) {
        return mongoTemplate.find(query, clazz);
    }

    public BaseServiceImpl() {
        clazz = AppReflectUtil.findTypeParam(this, BaseServiceImpl.class, "T");
    }
}
