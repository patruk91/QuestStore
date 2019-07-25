package com.codecool.server;

import com.codecool.dao.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private IArtifactDao artifactDao;
    private IClassDao classDao;
    private ICollectionGroupDao collectionGroupDao;
    private IExpLevelDao expLevelDao;
    private ILoginDao loginDao;
    private IMentorDao mentorDao;
    private IQuestDao questDao;
    private IStudentDao studentDao;
    private ISessionDao sessionDao;

    public Server(IArtifactDao artifactDao, IClassDao classDao, ICollectionGroupDao collectionGroupDao,
                  IExpLevelDao expLevelDao, ILoginDao loginDao, IMentorDao mentorDao, IQuestDao questDao,
                  IStudentDao studentDao, ISessionDao sessionDao) {
        this.artifactDao = artifactDao;
        this.classDao = classDao;
        this.collectionGroupDao = collectionGroupDao;
        this.expLevelDao = expLevelDao;
        this.loginDao = loginDao;
        this.mentorDao = mentorDao;
        this.questDao = questDao;
        this.studentDao = studentDao;
        this.sessionDao = sessionDao;
    }

    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/", new LoginHandler(sessionDao, loginDao));
        server.createContext("/static", new StaticHandler());
        server.createContext("/admin", new AdminHandler());
        server.createContext("/mentor", new MentorHandler());
        server.createContext("/student", new StudentHandler());
        server.createContext("/logout", new LogoutHandler());
        server.setExecutor(null);

        server.start();
    }
}
