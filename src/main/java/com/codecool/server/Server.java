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

    public Server(IArtifactDao artifactDao, IClassDao classDao, ICollectionGroupDao collectionGroupDao,
                  IExpLevelDao expLevelDao, ILoginDao loginDao, IMentorDao mentorDao, IQuestDao questDao,
                  IStudentDao studentDao) {
        this.artifactDao = artifactDao;
        this.classDao = classDao;
        this.collectionGroupDao = collectionGroupDao;
        this.expLevelDao = expLevelDao;
        this.loginDao = loginDao;
        this.mentorDao = mentorDao;
        this.questDao = questDao;
        this.studentDao = studentDao;
    }

    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/", new LoginHandler());
        server.setExecutor(null);

        server.start();
    }
}
