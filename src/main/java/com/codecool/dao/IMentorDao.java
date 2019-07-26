package com.codecool.dao;

import com.codecool.model.Mentor;

import java.util.List;

public interface IMentorDao {
    public List<Mentor> getAllMentors();
    public Mentor getMentor(int id);
    public void updateMentor(Mentor mentor);
    public void addMentor(Mentor mentor);
    public void removeMentor(Mentor mentor);
}
