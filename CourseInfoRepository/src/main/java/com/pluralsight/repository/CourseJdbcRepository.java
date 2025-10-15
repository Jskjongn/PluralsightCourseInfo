package com.pluralsight.repository;

import com.pluralsight.domain.Course;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class CourseJdbcRepository implements CourseRepository{

    private static final String URL = "jdbc:h2:file:%s;AUTO_SERVER=TRUE;INIT=RUNSCRIPT FROM './db_init.sql'";

    private final DataSource dataSource;

    public CourseJdbcRepository(String databaseFile) {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL(URL.formatted(databaseFile));
        this.dataSource = jdbcDataSource;
    }

    @Override
    public void saveCourse(Course course) {

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        MERGE INTO Courses (id, name, length, url)
                            VALUES (?, ?, ?, ?);
                        """)
        ) {
            preparedStatement.setString(1, course.id());
            preparedStatement.setString(2, course.name());
            preparedStatement.setLong(3, course.length());
            preparedStatement.setString(4, course.url());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // custom exception
            throw new RepositoryException("Failed to save " + course, e);
        }
    }

    @Override
    public List<Course> getAllCourses() {

        List<Course> courses = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT
                            *
                        FROM
                            Courses;
                        """)
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Course course = new Course(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getLong(3),
                            resultSet.getString(4),
                            Optional.ofNullable(resultSet.getString(5)));
                    courses.add(course);
                }
            }

           return Collections.unmodifiableList(courses);

        } catch (SQLException e) {
            // custom exception
            throw new RepositoryException("Failed to retrieve courses", e);
        }
    }

    @Override
    public void addNotes(String id, String notes) {

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        UPDATE Courses SET notes = ?
                            WHERE id = ?;
                        """)
        ) {
            preparedStatement.setString(1, notes);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // custom exception
            throw new RepositoryException("Failed to add notes to" + id, e);
        }
    }
}
