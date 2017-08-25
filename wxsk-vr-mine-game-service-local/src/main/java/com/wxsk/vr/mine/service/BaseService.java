package com.wxsk.vr.mine.service;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * 基础服务
 */
public interface BaseService<T> {

    /**
     * insert
     * */
    void insert(T t);

    /**
     * save
     * */
    void save(T t);

    /**
     * remove
     * */
    WriteResult remove(T t);

    /**
     * update first
     * */
    WriteResult updateFirst(Query query, Update update);

    /**
     * query by id
     * */
    T queryById(ObjectId id);

    /**
     * query by param
     * */
    List<T> queryByParam(Query query);


    abstract class BaseParam {

        /**
         * id
         * */
        protected ObjectId id;

        /**
         * skip
         * */
        protected int skip;

        /**
         * limit
         * */
        protected int limit;

        public ObjectId getId() {
            return id;
        }

        public BaseParam setId(ObjectId id) {
            this.id = id;
            return this;
        }

        public int getSkip() {
            return skip;
        }

        public BaseParam setSkip(int skip) {
            this.skip = skip;
            return this;
        }

        public int getLimit() {
            return limit;
        }

        public BaseParam setLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Query buildQuery() {
            Query query = new Query();
            Criteria criteria = new Criteria();
            query.addCriteria(criteria);
            if (id != null) {
                criteria.and("_id").is(id);
            }
            buildSubCriteria(criteria);
            if (skip > 0) {
                query.skip(skip);
            }
            if (limit > 0) {
                query.limit(limit);
            }
            return query;
        }
        abstract void buildSubCriteria(final Criteria criteria);
    }

}
