package com.codecool.dao.sql;

import com.codecool.dao.IStudentDao;
import com.codecool.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentSQL implements IStudentDao{
    private ConnectionPool connectionPool;

    public StudentSQL(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void addStudent(Student student, String salt) {
        try {
            Connection connection = connectionPool.getConnection();
            addStudentToDatabase(student, connection, salt);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void addStudentToDatabase(Student student, Connection connection, String salt) throws SQLException {
        try (PreparedStatement stmtInsertUserData = connection.prepareStatement(
            "INSERT INTO users(type, first_name, last_name, email) VALUES(?, ?, ?, ?)");
             PreparedStatement stmtInsertUserCredentials = connection.prepareStatement(
                     "INSERT INTO user_credentials(user_id, login, salt, password) VALUES(?, ?, ?, ?)");
             PreparedStatement stmtInsertStudentProfiles = connection.prepareStatement(
                     "INSERT INTO students_profiles(student_id, class_id, coins, experience) VALUES(?, ?, ?, ?)")) {
            insertUserData(stmtInsertUserData, student);
            int newUserId = getNewStudentId();
            stmtInsertUserCredentials(stmtInsertUserCredentials, student, newUserId, salt);
            insertStudentProfiles(stmtInsertStudentProfiles, student, newUserId);
        }
    }

    private void insertUserData(PreparedStatement stmtInsertUserData, Student student) throws SQLException {
        stmtInsertUserData.setString(1, student.getType());
        stmtInsertUserData.setString(2, student.getFirstName());
        stmtInsertUserData.setString(3, student.getLastName());
        stmtInsertUserData.setString(4, student.getEmail());
        stmtInsertUserData.executeUpdate();
    }

    private void stmtInsertUserCredentials(PreparedStatement stmtInsertUserCredentials, Student student, int newUserId, String salt) throws SQLException {
        stmtInsertUserCredentials.setInt(1, newUserId);
        stmtInsertUserCredentials.setString(2, student.getLogin());
        stmtInsertUserCredentials.setString(3, student.getPassword());
        stmtInsertUserCredentials.setString(4, salt);
        stmtInsertUserCredentials.executeUpdate();
    }

    private void insertStudentProfiles(PreparedStatement stmtInsertStudentProfiles, Student student, int newUserId) throws SQLException {
        stmtInsertStudentProfiles.setInt(1, newUserId);
        stmtInsertStudentProfiles.setInt(2, student.getClassId());
        stmtInsertStudentProfiles.setInt(3, student.getCoins());
        stmtInsertStudentProfiles.setInt(4, student.getExperience());
        stmtInsertStudentProfiles.executeUpdate();
    }

    @Override
    public void updateStudent(Student student) {
        try {
            Connection connection = connectionPool.getConnection();
            updateStudentData(connection, student);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void updateStudentData(Connection connection, Student student) throws SQLException {
        try (PreparedStatement stmtUpdateUserData = connection.prepareStatement(
                "UPDATE users SET type = ?, first_name = ?, last_name = ?, email = ? WHERE id = ?");
             PreparedStatement stmtUpdateUserCredentials = connection.prepareStatement(
                     "UPDATE user_credentials SET password = ? WHERE login = ?");
             PreparedStatement stmtUpdateStudentProfile = connection.prepareStatement(
                     "UPDATE students_profiles SET class_id = ?, coins = ?, experience = ? WHERE student_id = ?")) {
            updateUserData(stmtUpdateUserData, student);
            updateUserCredentials(stmtUpdateUserCredentials, student);
            updateStudentProfile(stmtUpdateStudentProfile, student);
        }
    }

    private void updateUserData(PreparedStatement stmtUpdateUserData, Student student) throws SQLException {
        stmtUpdateUserData.setString(1, student.getType());
        stmtUpdateUserData.setString(2, student.getFirstName());
        stmtUpdateUserData.setString(3, student.getLastName());
        stmtUpdateUserData.setString(4, student.getEmail());
        stmtUpdateUserData.setInt(5, student.getId());
        stmtUpdateUserData.executeUpdate();
    }

    private void updateUserCredentials(PreparedStatement stmtUpdateUserCredentials, Student student) throws SQLException {
        stmtUpdateUserCredentials.setString(1, student.getPassword());
        stmtUpdateUserCredentials.setString(2, student.getLogin());
        stmtUpdateUserCredentials.executeUpdate();
    }

    private void updateStudentProfile(PreparedStatement stmtUpdateStudentProfile, Student student) throws SQLException {
        stmtUpdateStudentProfile.setInt(1, student.getClassId());
        stmtUpdateStudentProfile.setInt(2, student.getCoins());
        stmtUpdateStudentProfile.setInt(3, student.getExperience());
        stmtUpdateStudentProfile.setInt(4, student.getId());
        stmtUpdateStudentProfile.executeUpdate();
    }

    @Override
    public void deleteStudent(int studentId) {
        try {
            Connection connection = connectionPool.getConnection();
            removeStudentFromDatabase(connection, studentId);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void removeStudentFromDatabase(Connection connection, int studentId) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM users  WHERE id = ?")) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
        }
    }
    
    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            Connection connection = connectionPool.getConnection();
            addStudents(students, connection);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return students;
    }

    private void addStudents(List<Student> students, Connection connection) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement(
                "SELECT users.id, users.type, users.first_name, users.last_name,\n" +
                        "users.email, cred.login, cred.password, profile.coins, profile.experience,\n" +
                        "profile.class_id FROM users\n" +
                        "JOIN user_credentials AS cred ON users.id = cred.user_id\n" +
                        "JOIN students_profiles AS profile on users.id = profile.student_id\n" +
                        "WHERE type = 'student'")) {
            addStudentToList(stmt, students);
        }
    }

    private void addStudentToList(PreparedStatement stmt, List<Student> students) throws SQLException {
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                Student student = buildSingleStudent(resultSet);
                students.add(student);
            }
        }
    }

    private Student buildSingleStudent(ResultSet resultSet) throws SQLException {
        UserBuilder userBuilder = new UserBuilder();
        return userBuilder.buildSingleStudent(resultSet);
    }

    @Override
    public Student getStudent(int id) {
        Student student = null;
        try {
            Connection connection = connectionPool.getConnection();
            student = getSingleStudent(connection, id);
            connectionPool.releaseConnection(connection);
            return student;
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        throw new RuntimeException("No student by that id");
    }

    private Student getSingleStudent(Connection connection, int id) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement(
                "SELECT users.id, users.type, users.first_name, users.last_name,\n" +
                        "users.email, cred.login, cred.password, profile.coins, profile.experience,\n" +
                        "profile.class_id FROM users\n" +
                        "JOIN user_credentials AS cred ON users.id = cred.user_id\n" +
                        "JOIN students_profiles AS profile on users.id = profile.student_id\n" +
                        "WHERE id = ?")) {
            stmt.setInt(1, id);
            return getSingleStudentData(stmt);
        }
    }

    private Student getSingleStudentData(PreparedStatement stmt) throws SQLException {
        Student student = null;
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                student = buildSingleStudent(resultSet);
            }
            return student;
        }
    }

    @Override
    public int getNewStudentId() {
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

    @Override
    public int getStudentCoins(int studentId) {
        int coins = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT coins FROM students_profiles WHERE student_id = ?")) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    coins = rs.getInt("coins");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return coins;
    }

    @Override
    public boolean canStudentAfford(int studentId, int purchasePrice) {
        int studentCoins = getStudentCoins(studentId);
        return studentCoins >= purchasePrice;
    }

    @Override
    public void setCoinsAmountForStudent(int studentId, int coins) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE students_profiles SET coins = ? WHERE student_id = ?")) {
            stmt.setInt(1, coins);
            stmt.setInt(2, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }
}
