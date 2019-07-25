package com.codecool.server;

import com.codecool.dao.IClassDao;
import com.codecool.dao.IExpLevelDao;
import com.codecool.dao.IMentorDao;
import com.codecool.dao.ISessionDao;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class AdminHandler implements HttpHandler {
    private IClassDao classDao;
    private IExpLevelDao expLevelDao;
    private IMentorDao mentorDao;
    private ISessionDao sessionDao;

    public AdminHandler(IClassDao classDao, IExpLevelDao expLevelDao, IMentorDao mentorDao, ISessionDao sessionDao) {
        this.classDao = classDao;
        this.expLevelDao = expLevelDao;
        this.mentorDao = mentorDao;
        this.sessionDao = sessionDao;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        HttpCookie cookie;
        if (method.equals("GET")) {
            if (cookieStr != null) {
                cookie = HttpCookie.parse(cookieStr).get(0);
                if (sessionDao.isCurrentSession(cookie.getValue())) {
                    int userId = sessionDao.getUserIdBySessionId(cookie.getValue());
                    handleRequest(httpExchange, userId, method);

                } else {
                    redirectToUserPage(httpExchange, "/");
                }
            } else {
                redirectToUserPage(httpExchange, "/");

            }
        }
    }

    private void handleRequest(HttpExchange httpExchange, int userId, String method) throws IOException {
        String response = "";
        URI uri = httpExchange.getRequestURI();
        Map<String, String> parsedUri = parseURI(uri.getPath());
        String action = "index";
        int mentorId = 0;
        if(!parsedUri.isEmpty()) {
            action = parsedUri.keySet().iterator().next();
            mentorId = Integer.parseInt(parsedUri.get(action));
        }
        switch (action) {
            case "index":
                response = index();
                httpExchange.sendResponseHeaders(200, response.length());
                break;
            case "add":
                response = add(method, httpExchange);
                break;
            case "edit":
                response = edit(mentorId, method, httpExchange);
                break;
            case "delete":
                delete(mentorId, httpExchange);
                break;
            default:
                index();
                break;
        }
        sendResponse(httpExchange, response);
    }


    private Map<String, String> parseURI (String uri) {
        Map<String, String> parsedURI = new HashMap<>();
        String[] uriParts = uri.split("/");
        String action;
        String data;
        if(uriParts.length > 3) {
            action = uriParts[2];
            data = uriParts[3];
            parsedURI.put(action, data);

        } else if(uriParts.length > 2) {
            action = uriParts[2];
            data = "0";
            parsedURI.put(action, data);
        }

        return parsedURI;
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void redirectToUserPage(HttpExchange httpExchange, String s) throws IOException {
        httpExchange.getResponseHeaders().set("Location", s);
        httpExchange.sendResponseHeaders(303, -1);
    }


    private String index() {
        return "";
    }

    private String classes() {
        return "";
    }

    private String experienceLevels() {
        return "";
    }

    private String add(String method, HttpExchange httpExchange) {
        return "";
    }

    private String edit(int mentorId, String method, HttpExchange httpExchange) {
        return "";
    }

    private String delete(int mentorId, HttpExchange httpExchange) {
        return "";
    }
}
