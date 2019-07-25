package com.codecool.server;

import com.codecool.dao.ISessionDao;
import com.codecool.dao.IStudentDao;
import com.codecool.server.helper.CommonHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MentorHandler implements HttpHandler {
    private IStudentDao studentDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;

    public MentorHandler(IStudentDao studentDao, ISessionDao sessionDao, CommonHelper commonHelper) {
        this.studentDao = studentDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
