package com.codecool.server;

import com.codecool.dao.ICollectionGroupDao;
import com.codecool.dao.ISessionDao;
import com.codecool.server.helper.CommonHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class StudentCollectionHandler implements HttpHandler {
    private ICollectionGroupDao collectionGroupDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;

    public StudentCollectionHandler(ICollectionGroupDao collectionGroupDao, ISessionDao sessionDao, CommonHelper commonHelper) {
        this.collectionGroupDao = collectionGroupDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
