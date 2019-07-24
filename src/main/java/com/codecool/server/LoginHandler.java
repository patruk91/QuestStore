package com.codecool.server;

import com.codecool.dao.ILoginDao;
import com.codecool.dao.ISessionDao;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;

public class LoginHandler implements HttpHandler {
    private ISessionDao sessionDao;

    public LoginHandler(ISessionDao sessionDao) {
        this.sessionDao = sessionDao;
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
                //redirect to website part(admin, mentor, student)
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
}
