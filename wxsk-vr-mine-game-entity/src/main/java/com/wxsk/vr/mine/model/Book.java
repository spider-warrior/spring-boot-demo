package com.wxsk.vr.mine.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Book extends BaseModel {

    private String name;

    private Integer type;

    private Student student;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", student=" + student +
                ", id=" + id +
                '}';
    }
}
