package com.codecool.dao.sql;

import com.codecool.dao.IClassDao;
import com.codecool.model.ClassGroup;
import com.codecool.model.Mentor;
import com.codecool.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ClassSQL implements IClassDao {
    private BasicConnectionPool connectionPool;

    public ClassSQL(BasicConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void addClass(ClassGroup classGroup) {
        String query = "INSERT INTO classes (class_name) VALUES (?)";

        try {
            Connection connection = connectionPool.getConnection();
            insertClassData(query, connection, classGroup);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void insertClassData(String query, Connection connection, ClassGroup classGroup) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, classGroup.getClassName());

            stmt.executeUpdate();
        }
    }

    @Override
    public void updateClass(ClassGroup classGroup) {

    }

    @Override
    public void removeClass(ClassGroup classGroup) {

    }

    @Override
    public List<ClassGroup> getAllClasses() {
        return null;
    }

    @Override
    public ClassGroup getClass(int classId) {
        return null;
    }

    @Override
    public String getClassName(Student student) {
        return null;
    }

    @Override
    public void addStudentToClass(Student student) {

    }

    @Override
    public void addMentorToClass(Mentor mentor) {

    }

    @Override
    public void getAllStudentsFromClass(Mentor mentor) {

    }
}
