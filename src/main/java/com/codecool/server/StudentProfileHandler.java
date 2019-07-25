package com.codecool.server;

import com.codecool.dao.IExpLevelDao;
import com.codecool.dao.ISessionDao;
import com.codecool.server.helper.CommonHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class StudentProfileHandler implements HttpHandler {
    private IExpLevelDao expLevelDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;

    public StudentProfileHandler(IExpLevelDao expLevelDao, ISessionDao sessionDao, CommonHelper commonHelper) {
        this.expLevelDao = expLevelDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
