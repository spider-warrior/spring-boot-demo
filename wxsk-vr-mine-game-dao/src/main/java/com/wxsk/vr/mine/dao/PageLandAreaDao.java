package com.wxsk.vr.mine.dao;

import com.wxsk.vr.mine.model.PageLandArea;
import org.springframework.data.mongodb.core.query.Criteria;

public interface PageLandAreaDao extends BaseDao<PageLandArea> {

    void removeByUserId(long userId);

    class PageLandAreaParam extends BaseParam {

        /**
         * userId
         * */
        private Long userId;

        /**
         * index
         * */
        private Integer index;

        @Override
        void buildSubCriteria(Criteria criteria) {
            if (userId != null) {
                criteria.and("userId").is(userId);
            }
            if (index != null) {
                criteria.and("index").is(index);
            }
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }
    }
}
