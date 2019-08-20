package com.codecool.server;

import com.codecool.dao.IExpLevelDao;
import com.codecool.dao.ISessionDao;
import com.codecool.server.helper.CommonHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Map;

public class AdminExpLevelsHandler implements HttpHandler {
    private IExpLevelDao expLevelDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;

    public AdminExpLevelsHandler(IExpLevelDao expLevelDao, ISessionDao sessionDao, CommonHelper commonHelper) {
        this.expLevelDao = expLevelDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        HttpCookie cookie;
        if (method.equals("GET") || method.equals("POST")) {
            if (cookieStr != null) {
                cookie = HttpCookie.parse(cookieStr).get(0);
                if (sessionDao.isCurrentSession(cookie.getValue())) {
                    int userId = sessionDao.getUserIdBySessionId(cookie.getValue());
                    response = handleRequest(httpExchange, userId, method);
                } else {
                    commonHelper.redirectToUserPage(httpExchange, "/");
                }
            } else {
                commonHelper.redirectToUserPage(httpExchange, "/");
            }
        }
        commonHelper.sendResponse(httpExchange, response);
    }

    private String handleRequest(HttpExchange httpExchange, int userId, String method) throws IOException {
        String response = "";
        URI uri = httpExchange.getRequestURI();
        Map<String, String> parsedUri = commonHelper.parseURI(uri.getPath());
        String action = "index";
        String expLevelName = "";
        if(!parsedUri.isEmpty()) {
            action = parsedUri.keySet().iterator().next();
            expLevelName = (parsedUri.get(action);
        }
        switch (action) {
            case "index":
                response = index(userId);
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                break;
            case "add":
                response = add(method, httpExchange);
                break;
            case "view":
                response = view(expLevelName, httpExchange);
                break;
            case "edit":
                response = edit(expLevelName, method, httpExchange);
                break;
            case "delete":
                delete(expLevelName, httpExchange);
                break;
            default:
                response = index(userId);
                break;
        }
        return response;
    }
}
