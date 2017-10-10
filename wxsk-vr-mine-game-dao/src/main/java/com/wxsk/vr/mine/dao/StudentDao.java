package com.wxsk.vr.mine.dao;

import com.wxsk.vr.mine.model.Student;
import org.springframework.data.mongodb.core.query.Criteria;

public interface StudentDao extends BaseDao<Student> {

    class StudentDaoParam extends BaseParam {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        void buildSubCriteria(Criteria criteria) {
            if (name != null) {
                criteria.and("name").is(name);
            }
        }
    }
}
