package com.pluralsight.CLI.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PluralsightCourseTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            00:05:37, 5
            01:08:54.9613330, 68
            00:00:00.0, 0
            """)
    void durationInMinutes_should_roundToCurrentMinute(String input, long expected) {
        PluralsightCourse course = new PluralsightCourse("id", "Test Course", input, "url.com", false);

        assertEquals(expected, course.durationInMinutes());
    }
}