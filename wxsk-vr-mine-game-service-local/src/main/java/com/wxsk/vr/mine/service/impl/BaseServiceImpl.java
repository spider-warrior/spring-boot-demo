package com.wxsk.vr.mine.service.impl;

import com.mongodb.WriteResult;
import com.wxsk.vr.mine.dao.BaseDao;
import com.wxsk.vr.mine.service.BaseService;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * 基础服务实现
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Override
    public void insert(T t) {
        getBaseDao().insert(t);
    }

    @Override
    public boolean update(T t) {
        WriteResult writeResult = getBaseDao().update(t);
        return writeResult.getN() > 0;
    }

    @Override
    public boolean remove(T t) {
        WriteResult writeResult = getBaseDao().remove(t);
        return writeResult.getN() > 0;
    }

    @Override
    public T queryById(ObjectId id) {
        return getBaseDao().queryById(id);
    }

    @Override
    public long countByParam(BaseDao.BaseParam param) {
        return getBaseDao().countByParam(param);
    }

    @Override
    public List<T> queryByParam(BaseDao.BaseParam param) {
        return getBaseDao().queryByParam(param);
    }

    @Override
    public void deleteByParam(BaseDao.BaseParam param) {
        getBaseDao().deleteByParam(param);
    }

    public BaseServiceImpl() {}

    public abstract BaseDao<T> getBaseDao();
}
