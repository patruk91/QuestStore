package com.codecool.dao;

import com.codecool.model.User;

public interface ILoginDao {
    public boolean checkIfCredentialsAreCorrect(String login, String password);
    public int getUserId(String login);
    public <T extends User> T getUserById(int userId);
}
