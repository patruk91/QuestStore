package com.codecool.dao;

import com.codecool.model.ClassGroup;

import java.util.List;

public interface IClassDao {
    public void addClass(ClassGroup classGroup);
    public void updateClass(ClassGroup classGroup);
    public void removeClass(ClassGroup classGroup);
    public List<ClassGroup> getAllClasses();
    public ClassGroup getClass(int classId);
}
