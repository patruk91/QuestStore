package com.codecool.dao;

import com.codecool.model.Student;

public interface IAbleToLearnDao {
    public String getClassName(Student student);
    public void addStudentToClass(Student student);
}
