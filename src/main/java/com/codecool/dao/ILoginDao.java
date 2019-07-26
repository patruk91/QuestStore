package com.codecool.dao;

import com.codecool.model.User;

public interface ILoginDao {
    int getUserId(String login);
    <T extends User> T getUserById(int userId);
    boolean checkIfLoginIsCorrect(String login);
    boolean checkIfPasswordIsCorrect(String login, String password);
    String getSaltByLogin(String login);
}
