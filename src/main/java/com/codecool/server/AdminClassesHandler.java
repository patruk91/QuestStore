package com.codecool.server;

import com.codecool.dao.IClassDao;
import com.codecool.dao.IExpLevelDao;
import com.codecool.dao.IMentorDao;
import com.codecool.dao.ISessionDao;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AdminClassesHandler implements HttpHandler {
    private IClassDao classDao;
    private IExpLevelDao expLevelDao;
    private IMentorDao mentorDao;
    private ISessionDao sessionDao;

    public AdminClassesHandler(IClassDao classDao, IExpLevelDao expLevelDao, IMentorDao mentorDao, ISessionDao sessionDao) {
        this.classDao = classDao;
        this.expLevelDao = expLevelDao;
        this.mentorDao = mentorDao;
        this.sessionDao = sessionDao;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
