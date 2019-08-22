package com.codecool.server;

import com.codecool.dao.ICollectionGroupDao;
import com.codecool.dao.IMentorDao;
import com.codecool.dao.ISessionDao;
import com.codecool.dao.IStudentDao;
import com.codecool.dao.sql.CollectionGroupSQL;
import com.codecool.model.CollectionGroup;
import com.codecool.server.helper.CommonHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentCollectionHandler implements HttpHandler {
    private ICollectionGroupDao collectionGroupDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;
    private IMentorDao mentorDao;
    private IStudentDao studentDao;

    public StudentCollectionHandler(ICollectionGroupDao collectionGroupDao, ISessionDao sessionDao,
                                    CommonHelper commonHelper, IMentorDao mentorDao, IStudentDao studentDao) {
        this.collectionGroupDao = collectionGroupDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
        this.mentorDao = mentorDao;
        this.studentDao = studentDao;
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
        int collectionId = 0;
        if(!parsedUri.isEmpty()) {
            action = parsedUri.keySet().iterator().next();
            collectionId = Integer.parseInt(parsedUri.get(action));
        }
        switch (action) {
            case "index":
                response = index(userId);
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                break;
            case "donate":
                response = donate(userId, httpExchange);
                break;
            default:
                response = index(userId);
                break;
        }
        return response;
    }

    private String index(int userId) {
        String fullName = String.format("%s %s", mentorDao.getMentor(userId).getFirstName(),
                mentorDao.getMentor(userId).getLastName());
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/collection.twig");
        JtwigModel model = JtwigModel.newModel();
        List<CollectionGroup> collections = collectionGroupDao.getAllCollection();
        int coins = studentDao.getStudentCoins(userId);

        model.with("coins", coins);
        model.with("fullName", fullName);
        model.with("collections", collections);
        String response = template.render(model);
        return response;
    }

    private String donate(int userId, HttpExchange httpExchange) throws IOException {
        String response = "";
        InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String formData = bufferedReader.readLine();
        Map<String, String> inputs = commonHelper.parseFormData(formData);

        int collectionId = Integer.parseInt(inputs.get("collectionId"));
        int donationAmount = Integer.parseInt(inputs.get("donationAmount"));

        if(studentDao.canStudentAfford(userId, donationAmount)){
            collectionGroupDao.donateToCollection(donationAmount, collectionId, userId);
        }

        commonHelper.redirectToUserPage(httpExchange, "/collection");
        return response;
    }
}
