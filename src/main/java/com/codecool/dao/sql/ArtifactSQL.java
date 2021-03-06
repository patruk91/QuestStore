package com.codecool.dao.sql;

import com.codecool.dao.IArtifactDao;
import com.codecool.model.Artifact;
import com.codecool.model.ArtifactCategoryEnum;
import com.codecool.model.QuestCategoryEnum;
import com.codecool.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtifactSQL implements IArtifactDao {
    private ConnectionPool connectionPool;

    public ArtifactSQL(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<Artifact> getAllArtifactsByStudentId(int studentId, boolean state) {
        List<Artifact> listOfQuests = new ArrayList<>();
        String query = "SELECT * FROM artifacts\n" +
                "JOIN user_artifacts\n" +
                "ON artifacts.id = user_artifacts.artifact_id\n" +
                "WHERE user_id = ? AND state = ?";

        try {
            Connection connection = connectionPool.getConnection();
            prepareQuestsForArtifactListQuery(listOfQuests, query, connection, studentId, state);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return listOfQuests;
    }

    private void prepareQuestsForArtifactListQuery(List<Artifact> listOfQuests, String query, Connection connection, int studentId, boolean state) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setBoolean(2, state);
            executeArtifactsListQuery(listOfQuests, stmt);
        }
    }

    private void executeArtifactsListQuery(List<Artifact> listOfQuests, PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int price = rs.getInt("price");
                String imageLink = rs.getString("image_link");
                ArtifactCategoryEnum category = ArtifactCategoryEnum.valueOf(rs.getString("category"));

                Artifact quest = new Artifact(id, name, description, price, imageLink, category);

                listOfQuests.add(quest);
            }
        }
    }

    @Override
    public void addArtifact(Artifact artifact) {
        try {
            Connection connection = connectionPool.getConnection();
            addArtifactToDatabase(artifact, connection);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void addArtifactToDatabase(Artifact artifact, Connection connection) throws SQLException {
        try (PreparedStatement stmtInsertArtifactData = connection.prepareStatement(
                "INSERT INTO artifacts(id, name, description, price, image_link, category) VALUES(?, ?, ?, ?, ?, ?)")) {
            insertArtifactData(stmtInsertArtifactData, artifact);
        }
    }

    private void insertArtifactData(PreparedStatement stmtInsertArtifactData, Artifact artifact) throws SQLException {
        stmtInsertArtifactData.setInt(1, artifact.getId());
        stmtInsertArtifactData.setString(2, artifact.getName());
        stmtInsertArtifactData.setString(3, artifact.getDescription());
        stmtInsertArtifactData.setInt(4, artifact.getPrice());
        stmtInsertArtifactData.setString(5, artifact.getImageLink());
        stmtInsertArtifactData.setString(6, artifact.getCategory().toString());
        stmtInsertArtifactData.executeUpdate();
    }

    @Override
    public void updateArtifact(Artifact artifact) {
        try {
            Connection connection = connectionPool.getConnection();
            updateArtifactData(connection, artifact);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void updateArtifactData(Connection connection, Artifact artifact) throws SQLException {
        try (PreparedStatement stmtUpdateUserData = connection.prepareStatement(
                "UPDATE artifacts SET name = ?, description = ?, price = ?, image_link = ?, category = ? WHERE id = ?")) {
            updateArtifactTable(stmtUpdateUserData, artifact);
        }
    }

    private void updateArtifactTable(PreparedStatement stmtUpdateUserData, Artifact artifact) throws SQLException {
        stmtUpdateUserData.setString(1, artifact.getName());
        stmtUpdateUserData.setString(2, artifact.getDescription());
        stmtUpdateUserData.setInt(3, artifact.getPrice());
        stmtUpdateUserData.setString(4, artifact.getImageLink());
        stmtUpdateUserData.setString(5, artifact.getCategory().toString());
        stmtUpdateUserData.setInt(6, artifact.getId());
        stmtUpdateUserData.executeUpdate();
    }

    @Override
    public void deleteArtifact(int artifactId) {
        try {
            Connection connection = connectionPool.getConnection();
            removeArtifactFromDatabase(connection, artifactId);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void removeArtifactFromDatabase(Connection connection, int artifactId) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM artifacts WHERE id = ?")) {
            stmt.setInt(1, artifactId);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Artifact> getAllArtifacts() {
        List<Artifact> artifacts = new ArrayList<>();
        try {
            Connection connection = connectionPool.getConnection();
            addArtifacts(artifacts, connection);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return artifacts;
    }

    private void addArtifacts(List<Artifact> artifacts, Connection connection) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM artifacts ORDER BY id")) {
            addArtifactsToList(stmt, artifacts);
        }
    }

    private void addArtifactsToList(PreparedStatement stmt, List<Artifact> artifacts) throws SQLException {
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                Artifact artifact = buildSingleArtifact(resultSet);
                artifacts.add(artifact);
            }
        }
    }

    private Artifact buildSingleArtifact(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        int price = resultSet.getInt("price");
        String image_link = resultSet.getString("image_link");
        ArtifactCategoryEnum category = ArtifactCategoryEnum.valueOf(resultSet.getString("category"));
        return new Artifact(id, name, description, price, image_link, category);
    }

    @Override
    public Artifact getArtifact(int id) {
        Artifact artifact = null;
        try {
            Connection connection = connectionPool.getConnection();
            artifact = getSingleArtifact(connection, id);
            connectionPool.releaseConnection(connection);
            return artifact;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        throw new RuntimeException("No artifact by that id");

    }

    private Artifact getSingleArtifact(Connection connection, int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM artifacts WHERE id = ?")) {
            stmt.setInt(1, id);
            return getSingleArtifactData(stmt);
        }
    }

    private Artifact getSingleArtifactData(PreparedStatement stmt) throws SQLException {
        Artifact artifact = null;
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                artifact = buildSingleArtifact(resultSet);
            }
            return artifact;
        }
    }

    @Override
    public Artifact getArtifact(String artifactName) {
        Artifact artifact = null;
        try {
            Connection connection = connectionPool.getConnection();
            artifact = getSingleArtifact(connection, artifactName);
            connectionPool.releaseConnection(connection);
            return artifact;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        throw new RuntimeException("No artifact by that id");

    }

    private Artifact getSingleArtifact(Connection connection, String artifactName) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM artifacts WHERE name = ?")) {
            stmt.setString(1, artifactName);
            return getSingleArtifactData(stmt);
        }
    }


    @Override
    public void buyArtifact(int studentId, int artifactId) {
        try {
            Connection connection = connectionPool.getConnection();
            addArtifactToUserArtifacts(studentId, connection, artifactId);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void addArtifactToUserArtifacts(int studentId, Connection connection, int artifactId) throws SQLException {
        try (PreparedStatement stmtInsertUserArtifactData = connection.prepareStatement(
                "INSERT INTO user_artifacts(user_id, artifact_id, date, state) VALUES(?, ?, ?, ?)")) {
            insertUserArtifactData(stmtInsertUserArtifactData, studentId, artifactId);
        }
    }

    private void insertUserArtifactData(PreparedStatement stmtInsertUserArtifactData, int studentId, int artifactId) throws SQLException {
        stmtInsertUserArtifactData.setInt(1, studentId);
        stmtInsertUserArtifactData.setInt(2, artifactId);
        stmtInsertUserArtifactData.setDate(3, new Date(System.currentTimeMillis()));
        stmtInsertUserArtifactData.setBoolean(4, false);
        stmtInsertUserArtifactData.executeUpdate();
    }

    @Override
    public void useArtifact(Student student, int artifactId) {
        try {
            Connection connection = connectionPool.getConnection();
            updateStateOfArtifact(student, artifactId, connection);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void updateStateOfArtifact(Student student, int artifactId, Connection connection) throws SQLException {
        try (PreparedStatement stmtArtifactData = connection.prepareStatement(
                "UPDATE user_artifacts SET state = ? WHERE artifact_id = ? AND user_id = ?")) {
            updateStateInDatabase(stmtArtifactData, student, artifactId);
        }
    }

    private void updateStateInDatabase(PreparedStatement stmtArtifactData, Student student, int artifactId) throws SQLException {
        stmtArtifactData.setBoolean(1, true);
        stmtArtifactData.setInt(2, artifactId);
        stmtArtifactData.setInt(3, student.getId());
        stmtArtifactData.executeUpdate();
    }
}


