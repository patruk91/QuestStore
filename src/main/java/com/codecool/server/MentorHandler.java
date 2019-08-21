package com.codecool.server;

import com.codecool.dao.*;
import com.codecool.model.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MentorHandler implements HttpHandler {
    private IStudentDao studentDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;
    private IMentorDao mentorDao;
    private IClassDao classDao;
    private IQuestDao questDao;
    private IArtifactDao artifactDao;

    public MentorHandler(IStudentDao studentDao,
                         ISessionDao sessionDao,
                         CommonHelper commonHelper,
                         IMentorDao mentorDao,
                         IClassDao classDao,
                         IQuestDao questDao,
                         IArtifactDao artifactDao) {
        this.studentDao = studentDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
        this.mentorDao = mentorDao;
        this.classDao = classDao;
        this.questDao = questDao;
        this.artifactDao = artifactDao;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        HttpCookie cookie;
        if (method.equals("GET")) {
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
        int studentId = 0;
        if(!parsedUri.isEmpty()) {
            action = parsedUri.keySet().iterator().next();
            studentId = Integer.parseInt(parsedUri.get(action));
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
                response = view(studentId, httpExchange);
                break;
            case "edit":
                response = edit(studentId, method, httpExchange);
                break;
            case "delete":
                delete(studentId, httpExchange);
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
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor.twig");
        JtwigModel model = JtwigModel.newModel();
        List<Student> students = studentDao.getAllStudents();
        HashMap<Integer, String> classesNames = new HashMap<>();
        for (Student student: students) {
            int id = student.getId();
            String className = classDao.getClassName(student);
            classesNames.put(id, className);
        }
        model.with("students", students);
        model.with("fullName", fullName);
        model.with("classesNames", classesNames);
        String response = template.render(model);
        return response;
    }

    private String add(String method, HttpExchange httpExchange) throws IOException {
        String response = "";
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/codecoolerDataForm.twig");
        JtwigModel model = JtwigModel.newModel();
        if (method.equals("GET")) {
            String operation = "add";
            String disabledAdd = "disabledAdd";
            String addCredentials = "addCredentials";
            List<ClassGroup> classes = classDao.getAllClasses();
            List<Quest> quests = questDao.getAllQuests();
            model.with("classes", classes);
            model.with("quests", quests);
            model.with("operation", operation);
            model.with("addCredentials", addCredentials);
            model.with("disabledAdd", disabledAdd);
            httpExchange.sendResponseHeaders(200, response.length());
            response = template.render(model);
        }

        if (method.equals("POST")) {
            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String formData = bufferedReader.readLine();
            Map<String, String> inputs = commonHelper.parseFormData(formData);


            String firstName = inputs.get("firstName");
            String lastName = inputs.get("lastName");
            String className = inputs.get("className");
            String login = inputs.get("login");
            String password = inputs.get("password");
            UserCredentials userCredentials = new UserCredentials(login, password);
            Student student = new Student.StudentBuilder()
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setUserCredentials(userCredentials).build();

            int newStudentId = studentDao.getNewStudentId();
            studentDao.addStudent(student);
            int classId = classDao.getClassId(className);
            classDao.addStudentToClass(newStudentId, classId);
            commonHelper.redirectToUserPage(httpExchange, "/mentor");
        }
        return response;
    }

    private String view(int studentId, HttpExchange httpExchange) throws IOException {
        String response = "";
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/codecoolerDataForm.twig");
        JtwigModel model = JtwigModel.newModel();
        String disabled = "disabled";
        Student student = studentDao.getStudent(studentId);
        String defaultClassName = classDao.getClassName(student);
        List<ClassGroup> classes = classDao.getAllClasses();
        List<Quest> quests = questDao.getAllQuests();
        List<Artifact> artifacts = artifactDao.getAllArtifactsByStudentId(studentId);

        model.with("student", student);
        model.with("classes", classes);
        model.with("quests", quests);
        model.with("artifacts", artifacts);
        model.with("defaultClassName", defaultClassName);
        model.with("disabled", disabled);


        httpExchange.sendResponseHeaders(200, response.length());
        response = template.render(model);
        return response;
    }

    private String edit(int studentId, String method, HttpExchange httpExchange) {
        return "";
    }

    private void delete(int studentId, HttpExchange httpExchange) {
    }
}
