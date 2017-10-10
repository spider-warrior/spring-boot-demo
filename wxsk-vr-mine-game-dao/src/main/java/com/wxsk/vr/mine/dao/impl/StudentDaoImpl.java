package com.wxsk.vr.mine.dao.impl;

import com.wxsk.vr.mine.dao.StudentDao;
import com.wxsk.vr.mine.model.Student;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDaoImpl extends BaseDaoImpl<Student> implements StudentDao {

    @Override
    public Update buildUpdate(Student student) {
        Update update = Update.update("name", student.getName());
        update.set("age", student.getAge());
        update.set("birthday", student.getBirthday());
        update.set("bookList", student.getBookList());
        return update;
    }
}
