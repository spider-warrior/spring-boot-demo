package com.wxsk.vr.mine.service.impl;

import com.wxsk.vr.mine.dao.BaseDao;
import com.wxsk.vr.mine.dao.StudentDao;
import com.wxsk.vr.mine.model.Student;
import com.wxsk.vr.mine.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends BaseServiceImpl<Student> implements StudentService {

    @Autowired
    private StudentDao studentDao;

    @Override
    public BaseDao<Student> getBaseDao() {
        return studentDao;
    }
}
