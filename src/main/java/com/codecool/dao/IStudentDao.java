package com.codecool.dao;

import com.codecool.model.Mentor;
import com.codecool.model.Student;

import java.util.List;

public interface IStudentDao {
    public void addStudent(Student student, String salt);
    public void updateStudent(Student student);
    public void deleteStudent(Student student);
    public List<Student> getAllStudents();
    public Student getStudent(int id);
    int getNewStudentId();
}
