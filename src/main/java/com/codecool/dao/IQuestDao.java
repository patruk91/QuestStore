package com.codecool.dao;

import com.codecool.model.Quest;
import com.codecool.model.Student;

import java.util.List;

public interface IQuestDao {
    public void addQuest(Quest quest);
    public void updateQuest(Quest quest);
    public void deleteQuest(Quest quest);
    public List<Quest> getAllQuests();
    public Quest getQuest(int id);
    public void asignQuest(Student student, int questId);
}
