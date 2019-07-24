package com.codecool.dao.sql;

import com.codecool.dao.ISessionDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionSQL implements ISessionDao {
    private ConnectionPool connectionPool;

    public SessionSQL(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void insertSessionId(String session_id, int userId) {
        try {
            Connection connection = connectionPool.getConnection();
            addUserSession(connection, session_id, userId);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void addUserSession(Connection connection, String session_id, int userId) throws SQLException {
        try (PreparedStatement stmtInsertUserSessionData = connection.prepareStatement(
                "INSERT INTO sessions(session_id, user_id) VALUES(?, ?)")) {
            insertUserSessionToDatabase(stmtInsertUserSessionData, session_id, userId);
        }
    }

    private void insertUserSessionToDatabase(PreparedStatement stmtInsertUserSessionData, String session_id, int userId) throws SQLException {
        stmtInsertUserSessionData.setString(1, session_id);
        stmtInsertUserSessionData.setInt(2, userId);
        stmtInsertUserSessionData.executeUpdate();
    }

    @Override
    public void deleteSessionId(String session_id) {
        try {
            Connection connection = connectionPool.getConnection();
            deleteUserSession(connection, session_id);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void deleteUserSession(Connection connection, String session_id) throws SQLException {
        try (PreparedStatement stmtInsertUserSessionData = connection.prepareStatement(
                "DELETE FROM sessions WHERE session_id = ?")) {
            stmtInsertUserSessionData.setString(1, session_id);
            stmtInsertUserSessionData.executeUpdate();
        }
    }

    @Override
    public boolean isCurrentSession(String session_id) {
        boolean exists = false;
        try {
            Connection connection = connectionPool.getConnection();
            exists = checkIfSessionExists(connection, session_id);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return exists;
    }

    private boolean checkIfSessionExists(Connection connection, String session_id) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement(
                "SELECT exists(SELECT 'exists' FROM sessions WHERE session_id = ?) AS result")) {
            stmt.setString(1, session_id);
            return isExists(stmt);
        }
    }

    private boolean isExists(PreparedStatement stmt) throws SQLException {
        boolean result = false;
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result = rs.getBoolean("result");
            }
        }
        return result;
    }

    @Override
    public int getUserIdBySessionId(String session_id) {
        int userId = 0;
        try {
            Connection connection = connectionPool.getConnection();
            userId = getUserId(connection, session_id, userId);
            connectionPool.releaseConnection(connection);
            return userId;
        } catch (SQLException e) {
            System.err.println("SQLException in selectUserIdBySessionId: " + e.getMessage());
        }
        throw new RuntimeException("No user_Id by that session_id");
    }

    private int getUserId(Connection connection, String session_id, int userId) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement("SELECT user_id FROM sessions WHERE session_id = ?")) {
            stmt.setString(1, session_id);
            return getUserIdFromDatabase(stmt, userId);
        }
    }

    private int getUserIdFromDatabase(PreparedStatement stmt, int userId) throws SQLException {
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                userId = resultSet.getInt("user_id");
            }
        }
        return userId;
    }


}
