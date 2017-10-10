package com.wxsk.vr.mine.dao;


import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BaseDao <T> {

    /**
     * insert
     */
    void insert(T t);

    /**
     * remove
     */
    WriteResult remove(T t);

    /**
     * remove by query
     * */
    WriteResult removeByQuery(Query query);

    /**
     * update first
     */
    WriteResult updateFirst(Query query, Update update);

    /**
     * update
     * */
    WriteResult update(T t);

    /**
     * query by id
     */
    T queryById(ObjectId id);

    /**
     * count by param
     * */
    long countByParam(BaseParam baseParam);

    /**
     * query list
     * */
    List<T> queryByParam(BaseParam baseParam);

    /**
     * query list
     * */
    List<T> queryByNativeQuery(Query query);

    /**
     * delete by param
     * */
    WriteResult deleteByParam(BaseParam baseParam);

    abstract class BaseParam {

        /**
         * id
         */
        protected ObjectId id;

        /**
         * skip
         */
        protected int skip;

        /**
         * limit
         */
        protected int limit;

        /**
         * 返回字段
         * */
        protected List<String> includeColumns = new ArrayList<>();

        /**
         * 限制条件
         * */
        private Map<String, Object> restrictions = new HashMap<>();

        /**
         * order by
         */
        protected List<Sort.Order> orderBy;

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

        public List<String> getIncludeColumns() {
            return includeColumns;
        }

        public void setIncludeColumns(List<String> includeColumns) {
            if (includeColumns != null) {
                this.includeColumns.addAll(includeColumns);
            }
        }

        public void setRestrictions(Map<String, Object> restrictions) {
            if (restrictions != null) {
                this.restrictions.putAll(restrictions);
            }
        }

        public void addIncludeColumn(String column) {
            this.includeColumns.add(column);
        }

        public void addRestriction(String key, Object value) {
            this.restrictions.put(key, value);
        }

        public List<Sort.Order> getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(List<Sort.Order> orderBy) {
            this.orderBy = orderBy;
        }

        public Query buildQuery() {
            Query query = buildCriteriaQuery();
            if (skip > 0) {
                query.skip(skip);
            }
            if (limit > 0) {
                query.limit(limit);
            }
            if (orderBy != null && orderBy.size() > 0) {
                for (Sort.Order order : orderBy) {
                    query.with(new Sort(order));
                }
            }
            for (String column: includeColumns) {
                query.fields().include(column);
            }
            return query;
        }

        public Query buildCriteriaQuery() {
            Query query = new Query();
            Criteria criteria = new Criteria();
            query.addCriteria(criteria);
            if (id != null) {
                criteria.and("_id").is(id);
            }
            if (restrictions.size() > 0) {
                for (Map.Entry<String, Object> entry: restrictions.entrySet()) {
                    criteria.and(entry.getKey()).is(entry.getValue());
                }
            }
            buildSubCriteria(criteria);
            return query;
        }

        abstract void buildSubCriteria(final Criteria criteria);

    }

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
