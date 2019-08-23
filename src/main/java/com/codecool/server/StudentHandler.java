package com.codecool.server;

import com.codecool.dao.IArtifactDao;
import com.codecool.dao.ICollectionGroupDao;
import com.codecool.dao.ISessionDao;
import com.codecool.dao.IStudentDao;
import com.codecool.model.Artifact;
import com.codecool.model.CollectionGroup;
import com.codecool.model.Quest;
import com.codecool.model.Student;
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
import java.util.List;
import java.util.Map;

public class StudentHandler implements HttpHandler {
    private IArtifactDao artifactDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;
    private IStudentDao studentDao;
    private ICollectionGroupDao collectionGroupDao;

    public StudentHandler(IArtifactDao artifactDao, ISessionDao sessionDao, CommonHelper commonHelper, IStudentDao studentDao, ICollectionGroupDao collectionGroupDao) {
        this.artifactDao = artifactDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
        this.studentDao = studentDao;
        this.collectionGroupDao = collectionGroupDao;
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
        int artifactId = 0;
        if(!parsedUri.isEmpty()) {
            action = parsedUri.keySet().iterator().next();
            artifactId = Integer.parseInt(parsedUri.get(action));
        }
        switch (action) {
            case "index":
                response = index(userId);
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                break;
            case "buy":
                buy(artifactId, userId, method, httpExchange);
                break;

            case "group":
                response ="";
                break;
            default:
                response = index(userId);
                break;
        }
        return response;
    }

    private String index(int userId) {
        String fullName = String.format("%s %s", studentDao.getStudent(userId).getFirstName(),
                studentDao.getStudent(userId).getLastName());
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/student.twig");
        JtwigModel model = JtwigModel.newModel();
        List<Artifact> artifacts = artifactDao.getAllArtifacts();
        int coins = studentDao.getStudentCoins(userId);

        model.with("coins", coins);
        model.with("artifacts", artifacts);
        model.with("fullName", fullName);
        String response = template.render(model);
        return response;
    }

    private void buy(int artifactId, int userId, String method, HttpExchange httpExchange) throws IOException {

        if (method.equals("POST")) {
            Artifact artifact = artifactDao.getArtifact(artifactId);
            if (artifact.getCategory().toString().equals("NORMAL")
                && studentDao.canStudentAfford(userId, artifact.getPrice())) {

                int studentCoins = studentDao.getStudentCoins(userId);
                int artifactPrice = artifact.getPrice();
                studentDao.setCoinsAmountForStudent(userId, studentCoins - artifactPrice);
                artifactDao.buyArtifact(userId, artifactId);
            } else if (artifact.getCategory().toString().equals("GROUP")) {

                InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String formData = bufferedReader.readLine();
                Map<String, String> inputs = commonHelper.parseFormData(formData);
                int donate = Integer.parseInt(inputs.get("donate"));
                String nameOfCollection = inputs.get("nameOfCollection");
                CollectionGroup collectionGroup = new CollectionGroup(artifact, nameOfCollection);
                if (studentDao.canStudentAfford(userId, donate)) {
                    collectionGroupDao.createCollection(collectionGroup);
                    collectionGroupDao.donateToCollection(donate, collectionGroupDao.getNewCollectionId(), userId);
                    studentDao.setCoinsAmountForStudent(userId, studentDao.getStudentCoins(userId) - donate);
                }
            }
        }
        commonHelper.redirectToUserPage(httpExchange, "/student");

    }
}
