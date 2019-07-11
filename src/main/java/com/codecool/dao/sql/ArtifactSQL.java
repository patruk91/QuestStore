package com.codecool.dao.sql;

import com.codecool.dao.IArtifactDao;
import com.codecool.model.Artifact;
import com.codecool.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        

    }

    @Override
    public List<Artifact> getAllArtifacts() {
        return null;
    }

    @Override
    public Artifact getArtifact(int id) {
        return null;
    }

    @Override
    public void buyArtifact(Student student, int artifactId) {

    }

    @Override
    public void useArtifact(Student student, int artifactId) {

    }
}
