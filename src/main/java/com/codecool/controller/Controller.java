package com.codecool.controller;

import com.codecool.dao.*;
import com.codecool.dao.sql.BasicConnectionPool;

public class Controller {
    private BasicConnectionPool connectionPool;
    private IArtifactDao artifactDao;
    private IClassDao classDao;
    private ICollectionGroupDao collectionGroupDao;
    private IExpLevelDao expLevelDao;
    private ILoginDao loginDao;
    private IMentorDao mentorDao;
    private IQuestDao questDao;
    private IStudentDao studentDao;

    public Controller(BasicConnectionPool connectionPool, IArtifactDao artifactDao, IClassDao classDao,
                      ICollectionGroupDao collectionGroupDao, IExpLevelDao expLevelDao, ILoginDao loginDao,
                      IMentorDao mentorDao, IQuestDao questDao, IStudentDao studentDao) {
        this.connectionPool = connectionPool;
        this.artifactDao = artifactDao;
        this.classDao = classDao;
        this.collectionGroupDao = collectionGroupDao;
        this.expLevelDao = expLevelDao;
        this.loginDao = loginDao;
        this.mentorDao = mentorDao;
        this.questDao = questDao;
        this.studentDao = studentDao;
    }

    public void run() {
    }
}
