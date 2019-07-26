package com.codecool.server;

import com.codecool.dao.IClassDao;
import com.codecool.dao.ISessionDao;
import com.codecool.server.helper.CommonHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AdminClassesHandler implements HttpHandler {
    private IClassDao classDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;

    public AdminClassesHandler(IClassDao classDao, ISessionDao sessionDao, CommonHelper commonHelper) {
        this.classDao = classDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
