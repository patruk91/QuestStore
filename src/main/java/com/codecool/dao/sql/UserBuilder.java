package com.codecool.dao.sql;

import com.codecool.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

class UserBuilder {
    Student buildSingleStudent(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String type = resultSet.getString("type");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        UserCredentials userCredentials = new UserCredentials(login, password);
        int coins = resultSet.getInt("coins");
        int experience = resultSet.getInt("experience");
        int classId = resultSet.getInt("experience");
        StudentProfile studentProfile = new StudentProfile(coins, experience, classId);

        Student.StudentBuilder studentBuilder = new Student.StudentBuilder();
        return studentBuilder
                .setId(id)
                .setType(type)
                .setUserCredentials(userCredentials)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setStudentProfile(studentProfile)
                .build();
    }

    Mentor buildSingleMentor(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String type = resultSet.getString("type");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        UserCredentials userCredentials = new UserCredentials(login, password);

        Mentor.MentorBuilder mentorBuilder = new Mentor.MentorBuilder();
        return mentorBuilder.setId(id)
                .setType(type)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setUserCredentials(userCredentials)
                .build();
    }

    Admin buildSingleAdmin(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String type = resultSet.getString("type");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        UserCredentials userCredentials = new UserCredentials(login, password);

        Admin.AdminBuilder adminBuilder = new Admin.AdminBuilder();
        return adminBuilder.setId(id)
                .setType(type)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setUserCredentials(userCredentials)
                .build();
    }
}
