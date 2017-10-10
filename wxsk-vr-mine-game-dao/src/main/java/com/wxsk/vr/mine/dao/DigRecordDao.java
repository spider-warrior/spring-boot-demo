package com.wxsk.vr.mine.dao;

import com.wxsk.vr.mine.model.DigRecord;
import org.springframework.data.mongodb.core.query.Criteria;

public interface DigRecordDao extends BaseDao<DigRecord>{

    /**
     * remove by user id
     * */
    void removeDigRecordByUserId(long userId);

    class DigRecordParam extends BaseParam{

        private Long userId;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        @Override
        void buildSubCriteria(final Criteria criteria) {
            if (userId != null) {
                criteria.and("userId").is(userId);
            }
        }
    }

}
