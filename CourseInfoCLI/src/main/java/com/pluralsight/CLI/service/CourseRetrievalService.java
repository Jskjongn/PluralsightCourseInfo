package com.pluralsight.CLI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CourseRetrievalService {

    // PS_URI = Pluralsight URI
    private static final String PS_URI = "https://app.pluralsight.com/profile/data/author/%s/all-content";

    private static final HttpClient CLIENT = HttpClient
            .newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    // Jackson Databind object
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // sends HTTP GET request to retrieve a list of Pluralsight courses for a given author
    public List<PluralsightCourse> getCourses(String authorId) {
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(PS_URI.formatted(authorId)))
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return switch (response.statusCode()) {
                // if response is 200 returns list of Pluralsight courses
                case 200 -> listPluralsightCourses(response);
                // if response is 404 returns empty list
                case 404 -> List.of();
                // else throws runtime exception with its following status code
                default -> throw new RuntimeException("Pluralsight API call failed with status code " + response.statusCode());
            };
            // pipe syntax to catch multiple exceptions
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Could not call Pluralsight API", e);
        }
    }

    // parses the JSON response body into a list of PluralsightCourse objects
    // the HTTP response containing a JSON array of PluralsightCourse data
    private static List<PluralsightCourse> listPluralsightCourses(HttpResponse<String> response) throws JsonProcessingException {
        // defines the type to deserialize the JSON into (PluralsightCourse objects)
        JavaType returnType = OBJECT_MAPPER.getTypeFactory()
                        .constructCollectionType(List.class, PluralsightCourse.class);
        // returns list of PluralsightCourse instances deserialized from the response body
        return OBJECT_MAPPER.readValue(response.body(), returnType);
    }
}
