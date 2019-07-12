package com.codecool.dao;

import com.codecool.model.ExpLevel;

import java.util.List;

public interface IExpLevelDao {
    void addExpLevel(ExpLevel expLevel);
    void updateExpLevel(ExpLevel expLevel);
    void removeLastExpLevel();
    List<ExpLevel> getAllExpLevels();
    ExpLevel getExpLevel(String expLevelName);
    String getExpLevelName(int expAmount);
}
