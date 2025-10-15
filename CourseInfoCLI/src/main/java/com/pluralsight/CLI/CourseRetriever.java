package com.pluralsight.CLI;

import com.pluralsight.CLI.service.CourseRetrievalService;
import com.pluralsight.CLI.service.CourseStorageService;
import com.pluralsight.CLI.service.PluralsightCourse;
import com.pluralsight.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class CourseRetriever {

    private static final Logger LOG = LoggerFactory.getLogger(CourseRetriever.class);

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {

        LOG.info("CourseRetriever starting");

        // single try catch to handle all exceptions if not handled else where later in code
        try {
            System.out.print("Please enter name of author: ");
            String authorName = SCANNER.nextLine().toLowerCase().trim();

            String[] nameParts = authorName.split(" ");

            String authorSlug = "";
            if (nameParts.length == 2) {
                authorSlug = nameParts[0] + "-" + nameParts[1];
            } else {
                LOG.warn("Please provide an author name");
                return;
            }

            retrieveCourses(authorSlug);

        } catch (Exception e) {
            LOG.error("Unexpected error", e);
        }
    }

    private static void retrieveCourses(String authorId) {
        LOG.info("Retrieving courses for author {}", authorId);
        CourseRetrievalService courseRetrievalService = new CourseRetrievalService();
        CourseRepository courseRepository = CourseRepository.openCourseRepository("./courses.db");
        CourseStorageService courseStorageService = new CourseStorageService(courseRepository);

        List<PluralsightCourse> coursesToStore = courseRetrievalService.getCourses(authorId)
                .stream()
                .filter(course -> !course.isRetired())
                .toList();
        LOG.info("Retrieved the following {} courses {}", coursesToStore.size(),coursesToStore);
        courseStorageService.storePluralsightCourses(coursesToStore);
        LOG.info("Courses successfully stored");
    }
}
