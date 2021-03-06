package com.codecool.dao;

import com.codecool.model.ClassGroup;
import com.codecool.model.Mentor;
import com.codecool.model.Student;

import java.util.List;

public interface IClassDao {
    public void addClass(String className);
    public void updateClass(ClassGroup classGroup);
    public void removeClass(int classId);
    public List<ClassGroup> getAllClasses();
    public List<ClassGroup> getAllUnassignedClasses();
    List<ClassGroup> getMentorClasses(int mentorId);
    public ClassGroup getClass(int classId);
    public String getClassName(Student student);
    void addStudentToClass(int studentId, int classId);
    public void addMentorToClass(int mentorId, int classId);
    public List<Student> getAllStudentsFromClass(Mentor mentor);
    public void updateMentorClasses(int mentorId, List<Integer> classesId);
    List<Student> getAllStudentsFromClass(int classId);
    void updateClass(String className, int classId);
    int getClassId(String className);
}
