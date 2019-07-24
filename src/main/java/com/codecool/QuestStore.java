package com.codecool;

import com.codecool.controller.Controller;
import com.codecool.dao.*;
import com.codecool.dao.sql.*;
import com.codecool.server.Server;

import java.sql.SQLException;

public class QuestStore {
    public static void main( String[] args ) {
        final String URL = "jdbc:postgresql://192.168.10.171:5432/queststore";
        final String USER = "queststore";
        final String PASSWORD = "queststore";

        BasicConnectionPool connectionPool = null;
        try {
            connectionPool = BasicConnectionPool.create(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        IArtifactDao artifactDao = new ArtifactSQL(connectionPool);
        IClassDao classDao = new ClassSQL(connectionPool);
        ICollectionGroupDao collectionGroupDao = new CollectionGroupSQL(connectionPool);
        IExpLevelDao expLevelDao = new ExpLevelSQL(connectionPool);
        ILoginDao loginDao = new LoginSQL(connectionPool);
        IMentorDao mentorDao = new MentorSQL(connectionPool);
        IQuestDao questDao = new QuestSQL(connectionPool);
        IStudentDao studentDao = new StudentSQL(connectionPool);

        Server server = new Server(artifactDao, classDao, collectionGroupDao, expLevelDao, loginDao, mentorDao, questDao, studentDao);

        Controller controller = new Controller(server);
        controller.run();
        try {
            connectionPool.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
