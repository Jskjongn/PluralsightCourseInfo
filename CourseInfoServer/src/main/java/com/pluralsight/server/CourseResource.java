package com.pluralsight.server;

import com.pluralsight.domain.Course;
import com.pluralsight.repository.CourseRepository;
import com.pluralsight.repository.RepositoryException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.stream.Stream;

@Path("/courses")
public class CourseResource {

    private static final Logger LOG = LoggerFactory.getLogger(CourseResource.class);

    private final CourseRepository courseRepository;

    public CourseResource(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Stream<Course> getCourses() {

        try {
            return courseRepository
                    .getAllCourses()
                    .stream()
                    .sorted(Comparator.comparing(Course::id));
        } catch (RepositoryException e) {
            LOG.error("Could not retrieve courses from the database", e);
            // throws 404 error
            throw new NotFoundException();
        }
    }

    @POST
    @Path("/{id}/notes")
    @Consumes(MediaType.TEXT_PLAIN)
    public void addNotes(@PathParam("id") String id, String notes) {
        courseRepository.addNotes(id, notes);
    }
}
