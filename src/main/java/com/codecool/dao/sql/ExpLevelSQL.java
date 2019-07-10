package com.codecool.dao.sql;

import com.codecool.dao.IExpLevelDao;
import com.codecool.model.ExpLevel;

import java.util.List;

public class ExpLevelSQL implements IExpLevelDao {
    private BasicConnectionPool connectionPool;

    public ExpLevelSQL(BasicConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void addExpLevel(ExpLevel expLevel) {

    }

    @Override
    public void updateExpLevel(ExpLevel expLevel) {

    }

    @Override
    public void removeLastExpLevel() {

    }

    @Override
    public List<ExpLevel> getAllExpLevels() {
        return null;
    }

    @Override
    public ExpLevel getExpLevel(int expLevelId) {
        return null;
    }
}
