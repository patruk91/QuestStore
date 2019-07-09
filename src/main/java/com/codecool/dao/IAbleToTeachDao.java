package com.codecool.dao;

import com.codecool.model.Mentor;

public interface IAbleToTeachDao {
    public void addMentorToClass(Mentor mentor);
    public void getAllStudentsFromClass(Mentor mentor);
}
