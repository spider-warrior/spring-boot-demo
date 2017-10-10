package com.wxsk.vr.mine.dao;

import com.wxsk.vr.mine.model.LandArea;
import org.springframework.data.mongodb.core.query.Criteria;

public interface LandAreaDao extends BaseDao<LandArea> {
    
    class LandAreaParam extends BaseParam{

        @Override
        void buildSubCriteria(final Criteria criteria) {
            // TODO: 17-9-15
        }
    }
}
