package com.codecool.server;

import com.codecool.dao.ISessionDao;
import com.codecool.server.helper.CommonHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;

public class LogoutHandler implements HttpHandler {
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;

    public LogoutHandler(ISessionDao sessionDao, CommonHelper commonHelper) {
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        HttpCookie cookie;
        if (cookieStr != null) {
            cookie = HttpCookie.parse(cookieStr).get(0);
            if (sessionDao.isCurrentSession(cookie.getValue())) {
                sessionDao.deleteSessionId(cookie.getValue());
                System.out.println(cookie.toString());
                httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString()+"; Max-Age=0");
                commonHelper.redirectToUserPage(httpExchange, "/");
            } else {
                commonHelper.redirectToUserPage(httpExchange, "/");
            }
        } else {
            commonHelper.redirectToUserPage(httpExchange, "/");
        }
        commonHelper.redirectToUserPage(httpExchange, "/");
    }
}
