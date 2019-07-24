package com.codecool.dao;

import com.codecool.model.User;

public interface ILoginDao {
    boolean checkIfCredentialsAreCorrect(String login, String password);
    int getUserId(String login);
    <T extends User> T getUserById(int userId);
}
