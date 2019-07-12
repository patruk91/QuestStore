package com.codecool.dao.sql;

import com.codecool.dao.IExpLevelDao;
import com.codecool.model.ExpLevel;
import com.codecool.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpLevelSQL implements IExpLevelDao {
    private BasicConnectionPool connectionPool;

    public ExpLevelSQL(BasicConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void addExpLevel(ExpLevel expLevel) {
        try {
            Connection connection = connectionPool.getConnection();
            addExpLevelToDatabase(expLevel, connection);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void addExpLevelToDatabase(ExpLevel expLevel, Connection connection) throws SQLException {
        try (PreparedStatement stmtInsertExpLevelData = connection.prepareStatement(
                "INSERT INTO experience_levels(name, exp_amount_at_start, exp_amount_at_end) VALUES(?, ?, ?)")) {
            insertExpLevel(stmtInsertExpLevelData, expLevel);
        }
    }

    private void insertExpLevel(PreparedStatement stmtInsertExpLevelData, ExpLevel expLevel) throws SQLException {
        stmtInsertExpLevelData.setString(1, expLevel.getName());
        stmtInsertExpLevelData.setInt(2, expLevel.getExpAmountAtStart());
        stmtInsertExpLevelData.setInt(3, expLevel.getExpAmountAtEnd());
        stmtInsertExpLevelData.executeUpdate();
    }

    @Override
    public void updateExpLevel(ExpLevel expLevel) {
        try {
            Connection connection = connectionPool.getConnection();
            updateExpLevelData(connection, expLevel);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void updateExpLevelData(Connection connection, ExpLevel expLevel) throws SQLException {
        try (PreparedStatement stmtUpdateExpLevelData = connection.prepareStatement(
                "UPDATE experience_levels SET exp_amount_at_start = ?, exp_amount_at_end = ? WHERE name = ?")) {
            updateExpLevelInDatabase(stmtUpdateExpLevelData, expLevel);
        }
    }

    private void updateExpLevelInDatabase(PreparedStatement stmtUpdateExpLevelData, ExpLevel expLevel) throws SQLException {
        stmtUpdateExpLevelData.setInt(1, expLevel.getExpAmountAtStart());
        stmtUpdateExpLevelData.setInt(2, expLevel.getExpAmountAtEnd());
        stmtUpdateExpLevelData.setString(3, expLevel.getName());
    }

    @Override
    public void removeLastExpLevel() {
        try {
            Connection connection = connectionPool.getConnection();
            removeLevelFromDatabase(connection);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void removeLevelFromDatabase(Connection connection) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM experience_levels  WHERE exp_id = (SELECT * FROM experience_levels\n" +
                        "ORDER BY exp_id DESC\n" +
                        "LIMIT 1)")) {
            stmt.executeUpdate();
        }
    }

    @Override
    public List<ExpLevel> getAllExpLevels() {
        List<ExpLevel> expLevels = new ArrayList<>();
        try {
            Connection connection = connectionPool.getConnection();
            addExpLevels(expLevels, connection);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return expLevels;
    }

    private void addExpLevels(List<ExpLevel> expLevels, Connection connection) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM experience_levels")) {
            addExpLevelToList(stmt, expLevels);
        }
    }

    private void addExpLevelToList(PreparedStatement stmt, List<ExpLevel> expLevels) throws SQLException{
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                ExpLevel expLevel = getSingleExpLevelFromDatabase(resultSet);
                expLevels.add(expLevel);
            }
        }
    }

    private ExpLevel getSingleExpLevelFromDatabase(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        int experienceAmountAtStart = resultSet.getInt("exp_amount_at_start");
        int experienceAmountAtEnd = resultSet.getInt("exp_amount_at_end");
        return new ExpLevel(name, experienceAmountAtStart, experienceAmountAtEnd);
    }

    @Override
    public ExpLevel getExpLevel(String expLevelName) {
        ExpLevel expLevel = null;
        try {
            Connection connection = connectionPool.getConnection();
            expLevel = getSingleExpLevel(connection, expLevelName);
            connectionPool.releaseConnection(connection);
            return expLevel;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        throw new RuntimeException("No experience level by that name!");

    }

    private ExpLevel getSingleExpLevel(Connection connection, String expLevelName) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM experience_levels WHERE name = ?")) {
            stmt.setString(1, expLevelName);
            return getSingleExpLevelData(stmt);
        }
    }

    private ExpLevel getSingleExpLevelData(PreparedStatement stmt) throws SQLException {
        ExpLevel expLevel = null;
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                expLevel = getSingleExpLevelFromDatabase(resultSet);
            }
            return expLevel;
        }
    }

    @Override
    public String getExpLevelName(int expAmount) {
        String expLevelName;
        try {
            Connection connection = connectionPool.getConnection();
            expLevelName = getName(connection, expAmount);
            connectionPool.releaseConnection(connection);
            return expLevelName;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        throw new RuntimeException("No experience level name!");
    }

    private String getName(Connection connection, int expAmount) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT name FROM experience_levels WHERE ? BETWEEN exp_amount_at_start AND exp_amount_at_end;")) {
            stmt.setInt(1, expAmount);
            return getNameFromDataBase(stmt);
        }
    }

    private String getNameFromDataBase(PreparedStatement stmt) throws SQLException {
        String expLevelName = null;
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                expLevelName = resultSet.getString("name");
            }
            return expLevelName;
        }
    }
}
