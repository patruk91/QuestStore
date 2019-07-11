package com.codecool.dao.sql;

import com.codecool.dao.IStudentDao;
import com.codecool.model.Student;
import com.codecool.model.UserCredentials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentSQL implements IStudentDao{
    private BasicConnectionPool connectionPool;

    public StudentSQL(BasicConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void addStudent(Student student) {

    }

    @Override
    public void updateStudent(Student student) {

    }

    @Override
    public void deleteStudent(Student student) {

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
                "SELECT users.id, users.type, users.credential_id, users.first_name, users.last_name,\n" +
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
        int id = resultSet.getInt("id");
        String type = resultSet.getString("type");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        UserCredentials userCredentials = new UserCredentials(login, password);
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        int coins = resultSet.getInt("coins");
        int experience = resultSet.getInt("experience");
        int classId = resultSet.getInt("experience");

        Student.StudentBuilder studentBuilder = new Student.StudentBuilder();
        return studentBuilder
                .setId(id)
                .setType(type)
                .setUserCredentials(userCredentials)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setClassId(coins)
                .setCoins(experience)
                .setExperience(classId).build();
    }

    @Override
    public Student getStudent(int id) {
        Student student = null;
        try {
            Connection connection = connectionPool.getConnection();
            student = getSingleStudent(connection, id);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return student;
    }

    private Student getSingleStudent(Connection connection, int id) throws SQLException {
        try(PreparedStatement stmt = connection.prepareStatement(
                "SELECT users.id, users.type, users.credential_id, users.first_name, users.last_name,\n" +
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
}
