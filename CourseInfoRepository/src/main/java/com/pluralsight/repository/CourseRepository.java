package com.pluralsight.repository;

import com.pluralsight.domain.Course;

import java.util.List;

public interface CourseRepository {

    void saveCourse (Course course);

    List<Course> getAllCourses();

    void addNotes(String id, String notes);

    static CourseRepository openCourseRepository(String databaseFile) {
        return new CourseJdbcRepository(databaseFile);
    }
}
