package com.codecool.dao.sql;

import com.codecool.dao.IQuestDao;
import com.codecool.model.Mentor;
import com.codecool.model.Quest;
import com.codecool.model.QuestCategoryEnum;
import com.codecool.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestSQL implements IQuestDao {
    private ConnectionPool connectionPool;

    public QuestSQL(ConnectionPool connectionPool) {
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
            stmt.setInt(6, quest.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteQuest(Quest quest) {
        String questsDataQuery = "DELETE FROM quests WHERE id = ?";

        try {
            Connection connection = connectionPool.getConnection();
            deleteQuestDataFromQuests(questsDataQuery, connection, quest);
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
                String name = rs.getString("name");
                String description = rs.getString("description");
                int price = rs.getInt("price");
                String imageLink = rs.getString("image_link");
                QuestCategoryEnum category = QuestCategoryEnum.valueOf(rs.getString("category"));

                Quest quest = new Quest(id, name, description, price, imageLink, category);

                listOfQuests.add(quest);
            }
        }
    }

    @Override
    public Quest getQuest(int id) {
        String query = "SELECT * WHERE id = ?";
        Quest quest = null;
        try {
            Connection connection = connectionPool.getConnection();
            quest = prepareMentorByIdQuery(quest, query, connection, id);
            connectionPool.releaseConnection(connection);
            return quest;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        throw  new RuntimeException("No quest by that id");
    }

    private Quest prepareMentorByIdQuery(Quest quest, String query, Connection connection, int quest_id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quest_id);
            quest = executeMentorByIdQuery(quest, stmt);
        }
        return quest;
    }

    private Quest executeMentorByIdQuery(Quest quest, PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int price = rs.getInt("price");
                String imageLink = rs.getString("image_link");
                QuestCategoryEnum category = QuestCategoryEnum.valueOf(rs.getString("category"));


                quest = new Quest(id, name, description, price, imageLink, category);
            }
            return quest;
        }
    }

    @Override
    public void assignQuest(Student student, Mentor mentor, int questId) {
        String query = "INSERT INTO granted_quests (student_id, mentor_id, quest_id, date)" +
                "VALUE (? ? ? ?)";

        try {
            Connection connection = connectionPool.getConnection();
            insertGrentedQuestData(query, connection, questId, student, mentor);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void insertGrentedQuestData(String query, Connection connection, int questId, Student student, Mentor mentor) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, student.getId());
            stmt.setInt(2, mentor.getId());
            stmt.setInt(3, questId);
            stmt.setDate(4, new Date(System.currentTimeMillis()));

            stmt.executeUpdate();
        }
    }
}
