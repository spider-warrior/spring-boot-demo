package com.wxsk.vr.mine.dao;

import com.wxsk.vr.mine.model.LandAreaType;
import org.springframework.data.mongodb.core.query.Criteria;

public interface LandAreaTypeDao extends BaseDao<LandAreaType> {

    class LandAreaTypeParam extends BaseParam{

        @Override
        void buildSubCriteria(final Criteria criteria) {
            // TODO: 17-9-15
        }
    }
}
