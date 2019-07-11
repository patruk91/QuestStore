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
    private BasicConnectionPool connectionPool;

    public MentorSQL(BasicConnectionPool connectionPool) {
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
    public Mentor getMentor(int id) {
        String query = "SELECT u.id, u.type, u.first_name, u.last_name, u.email, c.login, " +
                "c.password FROM users AS u JOIN user_credentials AS c ON u.id = c.user_id WHERE id = ?";
        Mentor mentor = null;
        try {
            Connection connection = connectionPool.getConnection();
            mentor = prepareMentorByIdQuery(mentor, query, connection, id);
            connectionPool.releaseConnection(connection);
            return mentor;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        throw  new RuntimeException("No mentor by that id");
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
        String credentialsQUery = "Update user_credentials SET password = ? WHERE login = ?";
        try {
            Connection connection = connectionPool.getConnection();
            updateMentorInUsersQuery(usersQuery, connection, mentor);
            updateMentorInCredentialsQuery(credentialsQUery, connection, mentor);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }

    }

    private void updateMentorInCredentialsQuery(String credentialsQUery, Connection connection, Mentor mentor) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(credentialsQUery)) {
            stmt.setString(1, mentor.getPassword());
            stmt.setString(2, mentor.getLogin());

            stmt.executeUpdate();
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
                "VALUES (?, ?, ?, ?) returning id";
        String usersCredentialsQuery = "INSERT INTO user_credentials (login, password) VALUES (?, ?, ?)";

        try {
            Connection connection = connectionPool.getConnection();
            int newUserID = InsertMentorInUsersQuery(usersQuery, connection, mentor);
            InsertMentorInCredentialsQuery(usersCredentialsQuery, connection, mentor, newUserID);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private int InsertMentorInUsersQuery(String usersQuery, Connection connection, Mentor mentor) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(usersQuery)) {
            stmt.setString(1, mentor.getType());
            stmt.setString(2, mentor.getFirstName());
            stmt.setString(3, mentor.getLastName());
            stmt.setString(4, mentor.getEmail());

            return stmt.executeUpdate();
        }
    }

    private void InsertMentorInCredentialsQuery(String usersCredentialsQuery, Connection connection, Mentor mentor, int newUserID) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(usersCredentialsQuery)) {
            stmt.setInt(2, newUserID);
            stmt.setString(1, mentor.getPassword());
            stmt.setString(2, mentor.getLogin());

            stmt.executeUpdate();
        }
    }

    @Override
    public void removeMentor(Mentor mentor) {
        String removeMentorFromUsers = "DELETE FROM users WHERE id = ? CASCADE";
        String removeMentorFromCredentials = "DELETE FROM user_credentials WHERE user_id = ?";
    }
}
