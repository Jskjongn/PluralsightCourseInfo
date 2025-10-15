package com.pluralsight.domain;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void id_should_throwExceptionWhenEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Course("", "Test", 10, "url.com", Optional.empty());
        });

        assertEquals("No value present", exception.getMessage());
    }

    @Test
    void name_should_throwExceptionWhenEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Course("id", "", 10, "url.com", Optional.empty());
        });

        assertEquals("No value present", exception.getMessage());
    }

    @Test
    void url_should_throwExceptionWhenEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Course("id", "Test", 10, "", Optional.empty());
        });

        assertEquals("No value present", exception.getMessage());
    }

    @Test
    void course_should_rejectNullComponents() {
        assertThrows(IllegalArgumentException.class, () ->
                new Course(null, null, 1, null, Optional.empty()));
    }

    @Test
    void course_should_rejectBlankNotes() {
        assertThrows(IllegalArgumentException.class, () ->
                new Course("id", "Test", 1, "url", Optional.of("")));
    }
}