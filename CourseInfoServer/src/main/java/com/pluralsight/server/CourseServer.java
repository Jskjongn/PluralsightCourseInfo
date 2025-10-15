package com.pluralsight.server;

import com.pluralsight.repository.CourseRepository;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.LogManager;

public class CourseServer {

    // redirects SDK Logger to SLF4J Logger to unify logging approach
    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }

    private static final Logger LOG = LoggerFactory.getLogger(CourseServer.class);

    private static final String BASE_URI = loadDatasourceURI();

    public static void main(String[] args) {

        String databaseFilename = loadDatabaseFilename();

        LOG.info("Starting HTTP server with database {}", databaseFilename);
        CourseRepository courseRepository = CourseRepository.openCourseRepository(databaseFilename);
        ResourceConfig config = new ResourceConfig().register(new CourseResource(courseRepository));

        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }

    private static String loadDatabaseFilename() {
        try (InputStream propertiesStream = CourseServer.class.getResourceAsStream("/server.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesStream);
            return properties.getProperty("course.info.database");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load database filename");
        }
    }

    private static String loadDatasourceURI() {
        try (InputStream propertiesStream = CourseServer.class.getResourceAsStream("/server.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesStream);
            return properties.getProperty("course.datasource.uri");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not connect to URI");
        }
    }
}
