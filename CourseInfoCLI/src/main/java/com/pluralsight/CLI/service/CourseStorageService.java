package com.pluralsight.CLI.service;

import com.pluralsight.domain.Course;
import com.pluralsight.repository.CourseRepository;

import java.util.List;
import java.util.Optional;

public class CourseStorageService {

    // contentUrl variable is only the URL path so we provide the base URL to concatenate with
    private static final String BASE_URL = "https://app.pluralsight.com";

    private final CourseRepository courseRepository;

    public CourseStorageService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void storePluralsightCourses(List<PluralsightCourse> psCourses) {
        for (PluralsightCourse psCourse: psCourses) {
            Course course = new Course(
                    psCourse.id(), psCourse.title(), psCourse.durationInMinutes(), BASE_URL + psCourse.contentUrl(), Optional.empty());
            courseRepository.saveCourse(course);
        }
    }
}
