package com.wxsk.vr.mine.dao;

import com.mongodb.WriteResult;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.model.PageLandArea;
import com.wxsk.vr.mine.model.UserGameData;
import org.springframework.data.mongodb.core.query.Criteria;

public interface UserGameDataDao extends BaseDao<UserGameData> {

    /**
     * 查询当前地块页信息
     * */
    PageLandArea queryUserCurrentPageLandArea(User user);

    /**
     * 查询当前地块信息
     * */
    LandArea queryUserCurrentLandArea(User user);

    /**
     * 查询当前地块页某个地块信息
     * */
    LandArea queryUserCurrentPageLandArea(User user, int index);

    /**
     * 更新当前地块页信息
     * */
    WriteResult updateUserCurrentPageLandArea(User user, PageLandArea currentPageLandArea);

    /**
     * 更新当前地块信息
     * */
    WriteResult updateUserCurrentLandArea(User user, LandArea currentLandArea);

    /**
     * 更新当前地块中某个地块信息
     * */
    WriteResult updateUserCurrentPageLandArea(User user, LandArea landArea);



    class UserGameDataParam extends BaseParam {

        /**
         * 用户id
         * */
        private Long userId;

        /**
         * 挖掘通知状态
         * */
        private Boolean informed;

        /**
         * 存在极光id
         * */
        private boolean jiguangIdNotNull;

        /**
         * 挖掘结束时间开始
         */
        private Long digEndTimeStart;

        /**
         * 挖掘结束时间结束
         */
        private Long digEndTimeEnd;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Boolean getInformed() {
            return informed;
        }

        public void setInformed(Boolean informed) {
            this.informed = informed;
        }

        public boolean isJiguangIdNotNull() {
            return jiguangIdNotNull;
        }

        public void setJiguangIdNotNull(boolean jiguangIdNotNull) {
            this.jiguangIdNotNull = jiguangIdNotNull;
        }

        public Long getDigEndTimeStart() {
            return digEndTimeStart;
        }

        public UserGameDataParam setDigEndTimeStart(Long digEndTimeStart) {
            this.digEndTimeStart = digEndTimeStart;
            return this;
        }

        public Long getDigEndTimeEnd() {
            return digEndTimeEnd;
        }

        public UserGameDataParam setDigEndTimeEnd(Long digEndTimeEnd) {
            this.digEndTimeEnd = digEndTimeEnd;
            return this;
        }

        @Override
        void buildSubCriteria(final Criteria criteria) {
            if (userId != null) {
                criteria.and("userId").is(userId);
            }
            if (informed != null) {
                criteria.and("digRecord").exists(true).and("digRecord.informed").is(false);
            }
            if (jiguangIdNotNull) {
                criteria.and("jiguangId").ne(null);
            }
            if (digEndTimeStart != null && digEndTimeEnd != null) {
                criteria.andOperator(Criteria.where("digRecord.endTime").gte(digEndTimeStart), Criteria.where("digRecord.endTime").lte(digEndTimeEnd));
            } else if (digEndTimeStart != null) {
                criteria.and("digRecord.endTime").gte(digEndTimeStart);
            } else if (digEndTimeEnd != null) {
                criteria.and("digRecord.endTime").lte(digEndTimeEnd);
            }
        }
    }
}
