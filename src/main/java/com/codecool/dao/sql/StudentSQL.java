package com.codecool.dao.sql;

import com.codecool.dao.IAbleToLearnDao;
import com.codecool.dao.IStudentDao;
import com.codecool.model.Mentor;
import com.codecool.model.Student;

import java.util.List;

public class StudentSQL implements IStudentDao, IAbleToLearnDao {
    @Override
    public String getClassName(Student student) {
        return null;
    }

    @Override
    public void addStudentToClass(Student student) {

    }

    @Override
    public void addStudent(Student student) {

    }

    @Override
    public void updateStudent(Student student) {

    }

    @Override
    public void deleteStudent(Student student) {

    }

    @Override
    public List<Student> getAllStudents(Mentor mentor) {
        return null;
    }

    @Override
    public Student getStudent(int id) {
        return null;
    }
}
