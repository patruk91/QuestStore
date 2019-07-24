package com.codecool.dao.sql;

import com.codecool.dao.ILoginDao;
import com.codecool.model.User;

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
        return false;
    }

    @Override
    public int getUserId(String login) {
        int userId = 0;
        try {
            Connection connection = connectionPool.getConnection();
            userId = getUserIdByLogin(login, connection);
            connectionPool.releaseConnection(connection);
            return userId;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        throw new RuntimeException("No user by that id");
    }

    private int getUserIdByLogin(String login, Connection connection) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement(
                "SELECT user_id FROM user_credentials WHERE login = ?")) {
            stmt.setString(1, login);
            return getUserIdFromDatabase(stmt);
        }
    }

    private int getUserIdFromDatabase(PreparedStatement stmt) throws SQLException {
        int userId = 0;
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                userId = resultSet.getInt(1);
            }
        }
        return userId;
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
                        "users.email, cred.login, cred.password, profile.coins, profile.experience,\n" +
                        "profile.class_id FROM users\n" +
                        "JOIN user_credentials AS cred ON users.id = cred.user_id\n" +
                        "LEFT JOIN students_profiles AS profile on users.id = profile.student_id\n" +
                        "WHERE type = ?\n")) {
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
}
