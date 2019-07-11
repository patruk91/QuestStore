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
import java.util.List;

public class CollectionGroupSQL implements ICollectionGroupDao {
    private BasicConnectionPool connectionPool;

    public CollectionGroupSQL(BasicConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<CollectionGroup> getAllCollection() {
        List<CollectionGroup> listOfCollections = new ArrayList<>();
        String query = "SELECT c.id, c.description, (SELECT SUM(donate_amount) FROM collection_donations " +
                "WHERE collection_id = c.id) AS coins_collected, a.id AS artifact_id, a.name, " +
                "a.description AS artifact_description, a.price, a.image_link, a.category " +
                "FROM collections AS c JOIN artifacts AS a ON c.artifact_id = a.id ORDER BY a.price";

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
    public void donateToCollection(int amount, CollectionGroup collectionGroup, Student student) {
        String query = "INSERT INTO collectionDonations (user_id, collection_id, donate_amount) VALUES (?, ?, ?)";

        try {
            Connection connection = connectionPool.getConnection();
            insertNewDonation(query, connection, collectionGroup, student, amount);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void insertNewDonation(String query, Connection connection, CollectionGroup collectionGroup,
                                   Student student, int amount) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, student.getId());
            stmt.setInt(2, collectionGroup.getCollectionId());
            stmt.setInt(3, amount);

            stmt.executeUpdate();
        }
    }

    @Override
    public void createCollection(CollectionGroup collection) {
        String query = "INSERT INTO collections (artifact_id, description) VALUES (?, ?)";

        try {
            Connection connection = connectionPool.getConnection();
            insertNewCollection(query, connection, collection);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void insertNewCollection(String query, Connection connection, CollectionGroup collection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, collection.getCollectionId());
            stmt.setString(2, collection.getNameOfCollection());

            stmt.executeUpdate();
        }
    }

    @Override
    public void removeCollection(CollectionGroup collection) {

    }
}
