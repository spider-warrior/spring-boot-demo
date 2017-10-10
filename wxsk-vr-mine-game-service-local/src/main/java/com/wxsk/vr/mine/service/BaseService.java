package com.wxsk.vr.mine.service;

import com.wxsk.vr.mine.dao.BaseDao;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * 基础服务
 */
public interface BaseService<T> {

    /**
     * insert
     */
    void insert(T t);

    /**
     * update
     * @return 是否更新成功
     */
    boolean update(T t);

    /**
     * remove
     * @return 是否删除成功
     */
    boolean remove(T t);

    /**
     * query by id
     */
    T queryById(ObjectId id);

    /**
     * count by param
     * */
    long countByParam(BaseDao.BaseParam param);

    /**
     * query by param
     */
    List<T> queryByParam(BaseDao.BaseParam param);

    /**
     * delete by param
     * */
    void deleteByParam(BaseDao.BaseParam param);

    abstract class MethodInvodeResult {

        protected boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

}
