package com.codecool.dao.sql;

import com.codecool.dao.IQuestDao;
import com.codecool.model.Mentor;
import com.codecool.model.Quest;
import com.codecool.model.QuestCategoryEnum;
import com.codecool.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestSQL implements IQuestDao {
    private BasicConnectionPool connectionPool;

    public QuestSQL(BasicConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void addQuest(Quest quest) {
        String query = "INSERT INTO quests (name, description, price, image_link, category) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = connectionPool.getConnection();
            insertQuestData(query, connection, quest);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void insertQuestData(String query, Connection connection, Quest quest) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, quest.getName());
            stmt.setString(2, quest.getDescription());
            stmt.setInt(3, quest.getPrice());
            stmt.setString(4, quest.getImageLink());
            stmt.setString(5, quest.getCategory().toString());

            stmt.executeUpdate();
        }
    }

    @Override
    public void updateQuest(Quest quest) {
        String query = "UPDATE quests SET name = ?, description = ?, price = ?, image_link = ?, category = ? WHERE id = ?";

        try {
            Connection connection = connectionPool.getConnection();
            updateQuestData(query, connection, quest);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void updateQuestData(String query, Connection connection, Quest quest) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, quest.getName());
            stmt.setString(2, quest.getDescription());
            stmt.setInt(3, quest.getPrice());
            stmt.setString(4, quest.getImageLink());
            stmt.setString(5, quest.getCategory().toString());
            stmt.setInt(5, quest.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteQuest(Quest quest) {
        String questsDataQuery = "DELETE FROM quests WHERE id = ?";
        String grentedQuestsQuery = "DELETE FROM granted_quests WHERE quest_id = ?";

        try {
            Connection connection = connectionPool.getConnection();
            deleteQuestDataFromQuests(questsDataQuery, connection, quest);
            deleteQuestDataFromQuests(grentedQuestsQuery, connection, quest);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void deleteQuestDataFromQuests(String query, Connection connection, Quest quest) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quest.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Quest> getAllQuests() {
        List<Quest> listOfQuests = new ArrayList<>();
        String query = "SELECT * FROM quests";

        try {
            Connection connection = connectionPool.getConnection();
            prepareQuestsListQuery(listOfQuests, query, connection);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }


        return listOfQuests;
    }

    private void prepareQuestsListQuery(List<Quest> listOfQuests, String query, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            executeQuestsListQuery(listOfQuests, stmt);
        }
    }

    private void executeQuestsListQuery(List<Quest> listOfQuests, PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("type");
                String description = rs.getString("first_name");
                int price = rs.getInt("last_name");
                String imageLink = rs.getString("email");
                QuestCategoryEnum category = QuestCategoryEnum.valueOf(rs.getString("login"));

                Quest quest = new Quest(id, name, description, price, imageLink, category);

                listOfQuests.add(quest);
            }
        }
    }

    @Override
    public Quest getQuest(int id) {
        return null;
    }

    @Override
    public void assignQuest(Student student, int questId) {

    }
}
