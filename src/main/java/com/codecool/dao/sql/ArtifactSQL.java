package com.codecool.dao.sql;

import com.codecool.dao.IArtifactDao;
import com.codecool.model.Artifact;
import com.codecool.model.ArtifactCategoryEnum;
import com.codecool.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtifactSQL implements IArtifactDao {
    private BasicConnectionPool connectionPool;

    public ArtifactSQL(BasicConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
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
    public void deleteArtifact(Artifact artifact) {
        try {
            Connection connection = connectionPool.getConnection();
            removeArtifactFromDatabase(connection, artifact);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void removeArtifactFromDatabase(Connection connection, Artifact artifact) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM artifacts WHERE id = ?")) {
            stmt.setInt(1, artifact.getId());
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
                "SELECT * FROM artifacts")) {
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
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return artifact;
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
    public void buyArtifact(Student student, int artifactId) {

    }

    @Override
    public void useArtifact(Student student, int artifactId) {

    }
}


