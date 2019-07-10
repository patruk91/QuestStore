package com.codecool;

import com.codecool.controller.Controller;
import com.codecool.dao.*;
import com.codecool.dao.sql.*;

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
        IArtifactDao artifactDao = new ArtifactSQL();
        IClassDao classDao = new ClassSQL();
        ICollectionGroupDao collectionGroupDao = new CollectionGroupSQL();
        IExpLevelDao expLevelDao = new ExpLevelSQL();
        ILoginDao loginDao = new LoginSQL();
        IMentorDao mentorDao = new MentorSQL();
        IQuestDao questDao = new QuestSQL();
        IStudentDao studentDao = new StudentSQL();

        Controller controller = new Controller(connectionPool, artifactDao, classDao, collectionGroupDao, expLevelDao, loginDao, mentorDao, questDao, studentDao);
        controller.run();

    }
}
