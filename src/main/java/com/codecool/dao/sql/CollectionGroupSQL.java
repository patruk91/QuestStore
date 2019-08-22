package com.codecool.dao.sql;

import com.codecool.dao.ICollectionGroupDao;
import com.codecool.model.Artifact;
import com.codecool.model.ArtifactCategoryEnum;
import com.codecool.model.CollectionGroup;
import com.codecool.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionGroupSQL implements ICollectionGroupDao {
    private ConnectionPool connectionPool;

    public CollectionGroupSQL(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<CollectionGroup> getAllCollection() {
        List<CollectionGroup> listOfCollections = new ArrayList<>();
        String query = "SELECT c.id, c.description, (SELECT SUM(donate_amount) FROM collection_donations " +
                "WHERE collection_id = c.id) AS coins_collected, a.id AS artifact_id, a.name, " +
                "a.description AS artifact_description, a.price, a.image_link, a.category " +
                "FROM collections AS c JOIN artifacts AS a ON c.artifact_id = a.id ORDER BY c.id";

        try {
            Connection connection = connectionPool.getConnection();
            prepareCollectionsListQuery(listOfCollections, query, connection);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }


        return listOfCollections;
    }

    private void prepareCollectionsListQuery(List<CollectionGroup> listOfCollections, String query, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            executeCollectionsListQuery(listOfCollections, stmt);
        }
    }

    private void executeCollectionsListQuery(List<CollectionGroup> listOfCollections, PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                int coinsCollected = rs.getInt("coins_collected");
                int artifactId = rs.getInt("artifact_id");
                String artifactName = rs.getString("name");
                String artifactDescription = rs.getString("artifact_description");
                int artifactPrice = rs.getInt("price");
                String imageLink = rs.getString("image_link");
                ArtifactCategoryEnum category = ArtifactCategoryEnum.valueOf(rs.getString("category"));

                Artifact artifact = new Artifact(artifactId, artifactName, artifactDescription, artifactPrice, imageLink, category);
                CollectionGroup collectionGroup = new CollectionGroup(id, coinsCollected, artifact, description);

                listOfCollections.add(collectionGroup);
            }
        }
    }

    @Override
    public void donateToCollection(int amount, int collectionGroupId, int studentId) {
        String query = "INSERT INTO collection_donations (user_id, collection_id, donate_amount) VALUES (?, ?, ?)";

        try {
            Connection connection = connectionPool.getConnection();
            insertNewDonation(query, connection, collectionGroupId, studentId, amount);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void insertNewDonation(String query, Connection connection, int collectionGroupId,
                                   int studentId, int amount) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, collectionGroupId);
            stmt.setInt(3, amount);

            stmt.executeUpdate();
        }
    }

    @Override
    public void createCollection(CollectionGroup collection) {
        String query = "INSERT INTO collections (artifact_id, description) VALUES (?, ?)";

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, collection.getCollectionId());
            stmt.setString(2, collection.getNameOfCollection());
            stmt.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    @Override
    public void removeCollection(int collectionId) {
        String query = "DELETE FROM collections WHERE  id = ?";

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, collectionId);
            stmt.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    @Override
    public CollectionGroup getCollection(int collectionId) {
        String query = "SELECT c.id, c.description, (SELECT SUM(donate_amount) FROM collection_donations " +
                "WHERE collection_id = c.id) AS coins_collected, a.id AS artifact_id, a.name, " +
                "a.description AS artifact_description, a.price, a.image_link, a.category " +
                "FROM collections AS c JOIN artifacts AS a ON c.artifact_id = a.id WHERE c.id = ? ORDER BY c.id";

        CollectionGroup collectionGroup = null;
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, collectionId);
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String description = rs.getString("description");
                    int coinsCollected = rs.getInt("coins_collected");
                    int artifactId = rs.getInt("artifact_id");
                    String artifactName = rs.getString("name");
                    String artifactDescription = rs.getString("artifact_description");
                    int artifactPrice = rs.getInt("price");
                    String imageLink = rs.getString("image_link");
                    ArtifactCategoryEnum category = ArtifactCategoryEnum.valueOf(rs.getString("category"));

                    Artifact artifact = new Artifact(artifactId, artifactName, artifactDescription, artifactPrice, imageLink, category);
                    collectionGroup = new CollectionGroup(id, coinsCollected, artifact, description);
                }
            }
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return collectionGroup;
    }

    @Override
    public Set<Integer> getDonators(int collectionId) {
        Set<Integer> donators = new HashSet<>();
        String query = "SELECT user_id FROM collection_donations WHERE collection_id = ?";
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, collectionId);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()) {
                    int donatorId = rs.getInt("user_id");
                    donators.add(donatorId);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return donators;
    }
}
