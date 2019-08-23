package com.codecool.dao.sql;

import com.codecool.dao.IMentorDao;
import com.codecool.model.Mentor;
import com.codecool.model.UserCredentials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MentorSQL implements IMentorDao {
    private ConnectionPool connectionPool;

    public MentorSQL(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<Mentor> getAllMentors() {
        List<Mentor> listOfMentors = new ArrayList<>();
        String query = "SELECT u.id, u.type, u.first_name, u.last_name, u.email, c.login, " +
                "c.password FROM users AS u JOIN user_credentials AS c ON u.id = c.user_id WHERE type = 'mentor'";

        try {
            Connection connection = connectionPool.getConnection();
            prepareMentorsListQuery(listOfMentors, query, connection);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }


        return listOfMentors;
    }

    private void prepareMentorsListQuery(List<Mentor> listOfMentors, String query, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            executeMentorsListQuery(listOfMentors, stmt);
        }
    }

    private void executeMentorsListQuery(List<Mentor> listOfMentors, PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String login = rs.getString("login");
                String password = rs.getString("password");

                UserCredentials userCredentials = new UserCredentials(login, password);

                Mentor.MentorBuilder mentorBuilder = new Mentor.MentorBuilder();
                Mentor mentor = mentorBuilder.setId(id)
                        .setType(type)
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setEmail(email)
                        .setUserCredentials(userCredentials)
                        .build();

                listOfMentors.add(mentor);
            }
        }
    }

    @Override
    public Mentor getMentor(int id) throws IllegalArgumentException {
        String query = "SELECT u.id, u.type, u.first_name, u.last_name, u.email, c.login, " +
                "c.password FROM users AS u JOIN user_credentials AS c ON u.id = c.user_id WHERE id = ?";
        Mentor mentor = null;
        try {
            Connection connection = connectionPool.getConnection();
            mentor = prepareMentorByIdQuery(mentor, query, connection, id);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        if (mentor != null) {
            return mentor;
        } else {
            throw new IllegalArgumentException("No mentor by that id!");
        }
    }

    private Mentor prepareMentorByIdQuery(Mentor mentor, String query, Connection connection, int mentor_id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, mentor_id);
            mentor = executeMentorByIdQuery(mentor, stmt);
        }
        return mentor;
    }

    private Mentor executeMentorByIdQuery(Mentor mentor, PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String login = rs.getString("login");
                String password = rs.getString("password");

                UserCredentials userCredentials = new UserCredentials(login, password);

                Mentor.MentorBuilder mentorBuilder = new Mentor.MentorBuilder();
                mentor = mentorBuilder.setId(id)
                        .setType(type)
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setEmail(email)
                        .setUserCredentials(userCredentials)
                        .build();
            }
            return mentor;
        }
    }

    @Override
    public void updateMentor(Mentor mentor) {
        String usersQuery = "UPDATE users SET type = ?, first_name = ?, last_name = ?, email = ? WHERE id = ?";

        try {
            Connection connection = connectionPool.getConnection();
            updateMentorInUsersQuery(usersQuery, connection, mentor);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }

    }

    private void updateMentorInUsersQuery(String query, Connection connection, Mentor mentor) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, mentor.getType());
            stmt.setString(2, mentor.getFirstName());
            stmt.setString(3, mentor.getLastName());
            stmt.setString(4, mentor.getEmail());
            stmt.setInt(5, mentor.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void addMentor(Mentor mentor) {
        String usersQuery = "INSERT INTO users (type, first_name, last_name, email)" +
                "VALUES (?, ?, ?, ?)";


        try {
            Connection connection = connectionPool.getConnection();
            insertMentorInUsersQuery(usersQuery, connection, mentor);
//            insertMentorInCredentialsQuery(usersCredentialsQuery, connection, mentor);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void insertMentorInUsersQuery(String usersQuery, Connection connection, Mentor mentor) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(usersQuery)) {
            stmt.setString(1, mentor.getType());
            stmt.setString(2, mentor.getFirstName());
            stmt.setString(3, mentor.getLastName());
            stmt.setString(4, mentor.getEmail());
            stmt.executeUpdate();
        }
    }

    public void insertMentorInCredentialsQuery(Mentor mentor, String salt) {
        String usersCredentialsQuery = "INSERT INTO user_credentials (user_id, login, salt, password) " +
                "VALUES ((SELECT id FROM users ORDER BY id DESC LIMIT 1), ?, ?, ?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(usersCredentialsQuery)) {
            stmt.setString(1, mentor.getLogin());
            stmt.setString(2,salt);
            stmt.setString(3, mentor.getPassword());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    @Override
    public void updateMentorCredentials(Mentor mentor, String salt) {
        String usersCredentialsQuery = "UPDATE user_credentials SET salt = ?, password = ? WHERE user_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(usersCredentialsQuery)) {
            stmt.setString(1, salt);
            stmt.setString(2, mentor.getPassword());
            stmt.setInt(3, mentor.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    @Override
    public void removeMentor(int mentorId) {
        String removeMentorFromUsers = "DELETE FROM users WHERE id = ?";

        try {
            Connection connection = connectionPool.getConnection();
            removeMentorFromUsersQuery(removeMentorFromUsers, connection, mentorId);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    @Override
    public int getNewMentorId() {
        int id = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                "SELECT id FROM users ORDER BY id DESC limit 1")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    id = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return id;
    }

    private void removeMentorFromUsersQuery(String removeMentorFromUsers, Connection connection, int mentorId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(removeMentorFromUsers)) {
            stmt.setInt(1, mentorId);

            stmt.executeUpdate();
        }
    }
}
