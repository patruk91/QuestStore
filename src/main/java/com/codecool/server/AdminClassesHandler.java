package com.codecool.server;

import com.codecool.dao.IClassDao;
import com.codecool.dao.ISessionDao;
import com.codecool.server.helper.CommonHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Map;

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
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        HttpCookie cookie;
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
    commonHelper.sendResponse(httpExchange, response);
    }

    private String handleRequest(HttpExchange httpExchange, int userId, String method) throws IOException {
        String response = "";
        URI uri = httpExchange.getRequestURI();
        Map<String, String> parsedUri = commonHelper.parseURI(uri.getPath());
        String action = "index";
        int classesId = 0;
        if(!parsedUri.isEmpty()) {
            action = parsedUri.keySet().iterator().next();
            classesId = Integer.parseInt(parsedUri.get(action));
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
                response = view(classesId, httpExchange);
                break;
            case "edit":
                response = edit(classesId, method, httpExchange);
                break;
            case "delete":
                delete(classesId, httpExchange);
                break;
            default:
                response = index(userId);
                break;
        }
        return response;
    }

    private void delete(int classesId, HttpExchange httpExchange) {
    }

    private String edit(int classesId, String method, HttpExchange httpExchange) {
    }

    private String view(int classesId, HttpExchange httpExchange) {
    }

    private String add(String method, HttpExchange httpExchange) {
    }

    private String index(int userId) {
    }
}
