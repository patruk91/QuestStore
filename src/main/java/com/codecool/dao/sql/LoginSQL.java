package com.codecool.dao.sql;

import com.codecool.dao.ILoginDao;
import com.codecool.model.User;

public class LoginSQL implements ILoginDao {
    private BasicConnectionPool connectionPool;

    public LoginSQL(BasicConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public boolean checkIfCredentialsAreCorrect(String login, String password) {
        return false;
    }

    @Override
    public int getUserId(String login) {
        return 0;
    }

    @Override
    public User getUserById(int userId) {
        return null;
    }
}
