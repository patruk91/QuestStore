package com.codecool.server;

import com.codecool.dao.*;
import com.codecool.server.helper.CommonHelper;
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
    private CommonHelper commonHelper;

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
        this.commonHelper = new CommonHelper();
    }

    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/", new LoginHandler(sessionDao, loginDao, commonHelper));
        server.createContext("/static", new StaticHandler());

        server.createContext("/admin", new AdminHandler(mentorDao, sessionDao, commonHelper, classDao));
        server.createContext("/classes", new AdminClassesHandler(classDao, sessionDao, commonHelper, mentorDao));
        server.createContext("/experienceLevels", new AdminExpLevelsHandler(expLevelDao, sessionDao, commonHelper, mentorDao));


        server.createContext("/mentor", new MentorHandler(studentDao, sessionDao, commonHelper, mentorDao, classDao, questDao, artifactDao));
        server.createContext("/quest", new MentorQuestHandler(questDao, sessionDao, commonHelper, mentorDao));
        server.createContext("/artifact", new MentorArtifactHandler(artifactDao, sessionDao, commonHelper, mentorDao));

        server.createContext("/student", new StudentHandler(artifactDao, sessionDao, commonHelper, studentDao));
        server.createContext("/collection", new StudentCollectionHandler(collectionGroupDao, sessionDao,
                commonHelper, mentorDao, studentDao, artifactDao));
        server.createContext("/profile", new StudentProfileHandler(expLevelDao, sessionDao, commonHelper,
                mentorDao, studentDao, classDao, artifactDao));

        server.createContext("/logout", new LogoutHandler(sessionDao, commonHelper));
        server.setExecutor(null);

        server.start();
    }
}
