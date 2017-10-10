package com.wxsk.vr.mine.dao;

import com.wxsk.vr.mine.model.MagicBox;
import org.springframework.data.mongodb.core.query.Criteria;

public interface MagicBoxDao extends BaseDao<MagicBox>{

    /**
     * 根据用户名移除
     * */
    void removeByUserId(long userId);

    /**
     * 计算宝箱数量
     * */
    long countMagicBoxByParam(MagicBoxParam magicBoxParam);

    class MagicBoxParam extends BaseParam{

        /**
         * userId
         * */
        private Long userId;

        /**
         * 是否使用
         * */
        private Boolean used;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Boolean getUsed() {
            return used;
        }

        public void setUsed(Boolean used) {
            this.used = used;
        }

        @Override
        void buildSubCriteria(final Criteria criteria) {
            if (userId != null) {
                criteria.and("userId").is(userId);
            }
            if (used != null) {
                if (!used) {
                    criteria.and("useTime").is(0);
                }
                else {
                    criteria.and("useTime").ne(0);
                }
            }
        }
    }

}
