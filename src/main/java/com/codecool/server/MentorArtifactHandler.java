package com.codecool.server;

import com.codecool.dao.IArtifactDao;
import com.codecool.dao.IMentorDao;
import com.codecool.dao.ISessionDao;
import com.codecool.model.Artifact;
import com.codecool.model.ArtifactCategoryEnum;
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

public class MentorArtifactHandler implements HttpHandler {
    private IArtifactDao artifactDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;
    private IMentorDao mentorDao;

    public MentorArtifactHandler(IArtifactDao artifactDao, ISessionDao sessionDao, CommonHelper commonHelper, IMentorDao mentorDao) {
        this.artifactDao = artifactDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
        this.mentorDao = mentorDao;
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
        int artifactId = 0;
        if(!parsedUri.isEmpty()) {
            action = parsedUri.keySet().iterator().next();
            artifactId = Integer.parseInt(parsedUri.get(action));
        }
        switch (action) {
            case "index":
                response = index(userId, httpExchange);
                break;
            case "add":
                response = add(method, httpExchange);
                break;
            case "view":
                response = view(artifactId, httpExchange);
                break;
            case "edit":
                response = edit(artifactId, method, httpExchange);
                break;
            case "delete":
                delete(artifactId, httpExchange);
                break;
            default:
                response = index(userId, httpExchange);
                break;
        }
        return response;
    }

    private String index(int userId, HttpExchange httpExchange) throws IOException {
        String fullName = String.format("%s %s", mentorDao.getMentor(userId).getFirstName(),
                mentorDao.getMentor(userId).getLastName());
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/artifacts.twig");
        JtwigModel model = JtwigModel.newModel();
        List<Artifact> artifacts = artifactDao.getAllArtifacts();
        model.with("artifacts", artifacts);
        model.with("fullName", fullName);
        String response = template.render(model);
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        return response;
    }

    private String view(int artifactId, HttpExchange httpExchange) throws IOException {
        String response = "";
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/artifactForm.twig");
        JtwigModel model = JtwigModel.newModel();
        String disabled = "disabled";
        Artifact artifact = artifactDao.getArtifact(artifactId);
        model.with("artifact", artifact);
        model.with("disabled", disabled);
        httpExchange.sendResponseHeaders(200, response.length());
        response = template.render(model);
        return response;
    }

    private void delete(int artifactId, HttpExchange httpExchange) throws IOException {
        artifactDao.deleteArtifact(artifactId);
        commonHelper.redirectToUserPage(httpExchange, "/artifact");
    }

    private String add(String method, HttpExchange httpExchange) throws IOException {
        String response = "";
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/artifactForm.twig");
        JtwigModel model = JtwigModel.newModel();
        if (method.equals("GET")) {
            String operation = "add";
            model.with("operation", operation);
            model.with("defaultImage", "/static/images/artifact.jpg");
            httpExchange.sendResponseHeaders(200, response.length());
            response = template.render(model);
        }

        if (method.equals("POST")) {
            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String formData = bufferedReader.readLine();
            Map<String, String> inputs = commonHelper.parseFormData(formData);

            String questName = inputs.get("artifactName");
            int coins = Integer.parseInt(inputs.get("coins"));
            ArtifactCategoryEnum category = ArtifactCategoryEnum.valueOf(inputs.get("category"));
            String questDescription = inputs.get("artifactDescription");

            Artifact artifact = new Artifact(questName, questDescription, coins, category);
            artifactDao.addArtifact(artifact);

            commonHelper.redirectToUserPage(httpExchange, "/artifact");
        }
        return response;
    }

    private String edit(int artifactId, String method, HttpExchange httpExchange) throws IOException {
        String response = "";
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/artifactForm.twig");
        JtwigModel model = JtwigModel.newModel();
        if (method.equals("GET")) {
            Artifact artifact = artifactDao.getArtifact(artifactId);
            String operation = "edit/" + artifactId;
            String operationDisabled = "disabled";
            model.with("artifact", artifact);
            model.with("operation", operation);
            httpExchange.sendResponseHeaders(200, response.length());
            response = template.render(model);
        }

        if (method.equals("POST")) {
            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String formData = bufferedReader.readLine();

            Map<String, String> inputs = commonHelper.parseFormData(formData);

            String questName = inputs.get("artifactName");
            int coins = Integer.parseInt(inputs.get("coins"));
            ArtifactCategoryEnum category = ArtifactCategoryEnum.valueOf(inputs.get("category"));
            String questDescription = inputs.get("artifactDescription");

            Artifact artifact = new Artifact(artifactId, questName, questDescription, coins, category);
            artifactDao.updateArtifact(artifact);

            commonHelper.redirectToUserPage(httpExchange, "/artifact");
        }
        return response;
    }
}
