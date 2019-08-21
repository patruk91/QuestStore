package com.codecool.server;

import com.codecool.dao.IMentorDao;
import com.codecool.dao.IQuestDao;
import com.codecool.dao.ISessionDao;
import com.codecool.model.Quest;
import com.codecool.model.QuestCategoryEnum;
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

public class MentorQuestHandler implements HttpHandler {
    private IQuestDao questDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;
    private IMentorDao mentorDao;

    public MentorQuestHandler(IQuestDao questDao, ISessionDao sessionDao, CommonHelper commonHelper, IMentorDao mentorDao) {
        this.questDao = questDao;
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
        int questId = 0;
        if(!parsedUri.isEmpty()) {
            action = parsedUri.keySet().iterator().next();
            questId = Integer.parseInt(parsedUri.get(action));
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
                response = view(questId, httpExchange);
                break;
            case "edit":
//                response = edit(questId, method, httpExchange);
                break;
            case "delete":
                delete(questId, httpExchange);
                break;
            default:
                response = index(userId);
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                break;
        }
        return response;
    }

    private String index(int userId) {
        String fullName = String.format("%s %s", mentorDao.getMentor(userId).getFirstName(),
                mentorDao.getMentor(userId).getLastName());
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/quests.twig");
        JtwigModel model = JtwigModel.newModel();
        List<Quest> quests = questDao.getAllQuests();
        model.with("quests", quests);
        model.with("fullName", fullName);
        String response = template.render(model);
        return response;
    }

    private String view(int questId, HttpExchange httpExchange) throws IOException {
        String response = "";
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/questForm.twig");
        JtwigModel model = JtwigModel.newModel();
        String disabled = "disabled";
        Quest quest = questDao.getQuest(questId);
        model.with("quest", quest);
        model.with("disabled", disabled);
        httpExchange.sendResponseHeaders(200, response.length());
        response = template.render(model);
        return response;
    }

    private void delete(int questId, HttpExchange httpExchange) throws IOException {
        questDao.deleteQuest(questId);
        commonHelper.redirectToUserPage(httpExchange, "/quest");
    }

    private String add(String method, HttpExchange httpExchange) throws IOException {
        String response = "";
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/questForm.twig");
        JtwigModel model = JtwigModel.newModel();
        if (method.equals("GET")) {
            String operation = "add";
            model.with("operation", operation);
            model.with("defaultImage", "/static/images/quest.jpg");
            httpExchange.sendResponseHeaders(200, response.length());
            response = template.render(model);
        }

        if (method.equals("POST")) {
            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String formData = bufferedReader.readLine();
            Map<String, String> inputs = commonHelper.parseFormData(formData);

            int defaultId = 0;
            String defaultImage = "/static/images/quest.jpg";
            String questName = inputs.get("questName");
            int coins = Integer.parseInt(inputs.get("coins"));
            QuestCategoryEnum category = QuestCategoryEnum.valueOf(inputs.get("category"));
            String questDescription = inputs.get("questDescription");

            Quest quest = new Quest(defaultId, questName,questDescription, coins, defaultImage, category);
            questDao.addQuest(quest);

            commonHelper.redirectToUserPage(httpExchange, "/quest");
        }
        return response;
    }
}
