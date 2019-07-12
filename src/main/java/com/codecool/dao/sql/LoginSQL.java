package com.codecool.dao.sql;

import com.codecool.dao.ILoginDao;
import com.codecool.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginSQL implements ILoginDao {
    private BasicConnectionPool connectionPool;

    public LoginSQL(BasicConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public boolean checkIfCredentialsAreCorrect(String login, String password) {
        try {
            Connection connection = connectionPool.getConnection();
            boolean exists = checkCredentials(login, password, connection);
            connectionPool.releaseConnection(connection);
            return exists;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return false;
    }

    private boolean checkCredentials(String login, String password, Connection connection) throws SQLException{
        try(PreparedStatement stmt = connection.prepareStatement(
                "SELECT exists (SELECT true from user_credentials WHERE login = ? AND password = ?)")) {
            stmt.setString(1, login);
            stmt.setString(2, password);
            return stmt.execute();
        }
    }

    @Override
    public int getUserId(String login) {
        return 0;
    }

    @Override
    public <T extends User> T getUserById(int userId) {
        try {
            Connection connection = connectionPool.getConnection();
            String userType = getUserTypeById(connection, userId);
            connectionPool.releaseConnection(connection);
            return getSingleUser(connection, userId, userType);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        throw new RuntimeException("No user by that id");
    }

    private String getUserTypeById(Connection connection, int userId) throws SQLException {
        String type = null;
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT type FROM users WHERE id = ?")) {
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                type = resultSet.getString(1);
            }
            return type;
        }
    }

    private <T extends User> T getSingleUser(Connection connection, int userId, String userType) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT users.id, users.type, users.first_name, users.last_name,\n" +
                        "users.email, cred.login, cred.password FROM users\n" +
                        "    JOIN user_credentials AS cred ON users.id = cred.user_id\n" +
                        "    WHERE type = ?")) {
            stmt.setString(1, userType);

            return getSingleUserData(stmt, userType);
        }
    }

    private <T extends User> T getSingleUserData(PreparedStatement stmt, String userType) throws SQLException {
        try (ResultSet resultSet = stmt.executeQuery()) {

            if (resultSet.next()) {
                UserBuilder userBuilder = new UserBuilder();
                if (userType.equalsIgnoreCase("admin")) {
                    return (T) userBuilder.buildSingleAdmin(resultSet);
                } else if (userType.equalsIgnoreCase("mentor")) {
                    return (T) userBuilder.buildSingleMentor(resultSet);
                } else {
                    return (T) userBuilder.buildSingleStudent(resultSet);
                }
            }
        }
        throw new RuntimeException("d");
    }












    private Mentor getSingleMentor(Connection connection, int userId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT users.id, users.type, users.first_name, users.last_name,\n" +
                        "users.email, cred.login, cred.password, profile.coins, profile.experience,\n" +
                        "profile.class_id FROM users\n" +
                        "JOIN user_credentials AS cred ON users.id = cred.user_id\n" +
                        "JOIN students_profiles AS profile on users.id = profile.student_id\n" +
                        "WHERE id = ?")) {
            stmt.setInt(1, userId);
            return getSingleMentorData(stmt);
        }
    }

    private Mentor getSingleMentorData(PreparedStatement stmt) throws SQLException {
        Mentor mentor = null;
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                UserBuilder userBuilder = new UserBuilder();
                mentor = userBuilder.buildSingleMentor(resultSet);
            }
            return mentor;
        }
    }

}


