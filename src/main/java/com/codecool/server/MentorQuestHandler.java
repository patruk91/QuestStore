package com.codecool.server;

import com.codecool.dao.IQuestDao;
import com.codecool.dao.ISessionDao;
import com.codecool.server.helper.CommonHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MentorQuestHandler implements HttpHandler {
    private IQuestDao questDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;

    public MentorQuestHandler(IQuestDao questDao, ISessionDao sessionDao, CommonHelper commonHelper) {
        this.questDao = questDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
