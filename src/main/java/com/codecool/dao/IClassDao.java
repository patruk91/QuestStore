package com.codecool.dao;

import com.codecool.model.ClassGroup;
import com.codecool.model.Mentor;
import com.codecool.model.Student;

import java.util.List;

public interface IClassDao {
    public void addClass(ClassGroup classGroup);
    public void updateClass(ClassGroup classGroup);
    public void removeClass(ClassGroup classGroup);
    public List<ClassGroup> getAllClasses();
    public ClassGroup getClass(int classId);
    public String getClassName(Student student);
    public void addStudentToClass(Student student);
    public void addMentorToClass(Mentor mentor);
    public void getAllStudentsFromClass(Mentor mentor);
}
