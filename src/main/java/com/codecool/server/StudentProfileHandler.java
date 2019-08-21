package com.codecool.server;

import com.codecool.dao.*;
import com.codecool.model.Artifact;
import com.codecool.model.ExpLevel;
import com.codecool.model.Student;
import com.codecool.server.helper.CommonHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class StudentProfileHandler implements HttpHandler {
    private IExpLevelDao expLevelDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;
    private IMentorDao mentorDao;
    private IStudentDao studentDao;
    private IClassDao classDao;
    private IArtifactDao artifactDao;

    public StudentProfileHandler(IExpLevelDao expLevelDao, ISessionDao sessionDao,
                                 CommonHelper commonHelper, IMentorDao mentorDao,
                                 IStudentDao studentDao, IClassDao classDao,
                                 IArtifactDao artifactDao) {
        this.expLevelDao = expLevelDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
        this.mentorDao = mentorDao;
        this.studentDao = studentDao;
        this.classDao = classDao;
        this.artifactDao = artifactDao;
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
        String fullName = String.format("%s %s", mentorDao.getMentor(userId).getFirstName(),
                mentorDao.getMentor(userId).getLastName());
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/profile.twig");
        JtwigModel model = JtwigModel.newModel();
        Student student = studentDao.getStudent(userId);
        String className = classDao.getClassName(student);
        String expLevelName = expLevelDao.getExpLevelName(student.getExperience());
        ExpLevel expLevel = expLevelDao.getExpLevel(expLevelName);
        int expAmountHave = student.getExperience() - expLevel.getExpAmountAtStart();
        int expPointsNeed = expLevel.getExpAmountAtEnd() - expAmountHave;
        int expPercentageAnount = expAmountHave / expLevel.getExpAmountAtEnd() * 100;
        List<Artifact> artifacts = artifactDao.getAllArtifactsByStudentId(userId, true);

        model.with("fullName", fullName);
        model.with("artifacts", artifacts);
        model.with("expPercentageAnount", expPercentageAnount);
        model.with("expPointAmount", student.getExperience());
        model.with("expPointsNeed", expPointsNeed);
        model.with("expLevelName", expLevelName);
        model.with("className", className);
        String response = template.render(model);
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        return response;
    }
}
