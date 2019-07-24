package com.codecool.dao;

public interface ISessionDao {
    void insertSessionId(String session, int userId);
    void deleteSessionId(String session);
    boolean isCurrentSession(String session);
    int getUserIdBySessionId(String session);
}
