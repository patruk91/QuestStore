package com.codecool.server;

import com.codecool.dao.ILoginDao;
import com.codecool.dao.ISessionDao;
import com.codecool.model.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {
    private ISessionDao sessionDao;
    private ILoginDao loginDao;

    public LoginHandler(ISessionDao sessionDao, ILoginDao loginDao) {
        this.sessionDao = sessionDao;
        this.loginDao = loginDao;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        HttpCookie cookie;
        if (method.equalsIgnoreCase("GET")) {
            if (cookieStr != null) {
                cookie = HttpCookie.parse(cookieStr).get(0);
                if (sessionDao.isCurrentSession(cookie.getValue())) {
                    int userId = sessionDao.getUserIdBySessionId(cookie.getValue());
                    String userType = loginDao.getUserById(userId).getType();
                    switch (userType) {
                        case "admin":
                            redirectToUserPage(httpExchange, "/admin");
                            break;
                        case "mentor":
                            redirectToUserPage(httpExchange, "/mentor");
                            break;
                        case "student":
                            redirectToUserPage(httpExchange, "/student");
                            break;
                    }
                } else {
                    response = getLoginFrom();
                    httpExchange.sendResponseHeaders(200, response.length());
                }
            } else {
                response = getLoginFrom();
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
            }
            sendResponse(httpExchange, response);
        }

        if (method.equalsIgnoreCase("POST")) {
            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String formData = bufferedReader.readLine();

            Map<String, String> inputs = parseFormData(formData);
            String login = inputs.get("login");
            String password = inputs.get("password");
            loginDao.
        }


    }

    private void redirectToUserPage(HttpExchange httpExchange, String s) throws IOException {
        httpExchange.getResponseHeaders().set("Location", s);
        httpExchange.sendResponseHeaders(303, -1);
    }

    private String getLoginFrom() {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/index.twig");
        JtwigModel model = JtwigModel.newModel();
        String response = template.render(model);
        System.out.println(response);
        return response;
    }

    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }
}
