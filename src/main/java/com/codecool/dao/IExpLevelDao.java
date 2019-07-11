package com.codecool.dao;

import com.codecool.model.ExpLevel;

import java.util.List;

public interface IExpLevelDao {
    public void addExpLevel(ExpLevel expLevel);
    public void updateExpLevel(ExpLevel expLevel);
    public void removeLastExpLevel();
    public List<ExpLevel> getAllExpLevels();
    public ExpLevel getExpLevel(String expLevelName);
}
