package com.pluralsight.CLI.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Duration;
import java.time.LocalTime;

// tells Jackson when deserialize into PluralSightCourse record to only look at the parameters listed
// and ignores other JSON properties it defaults to
@JsonIgnoreProperties(ignoreUnknown = true)
public record PluralsightCourse(String id, String title, String duration, String contentUrl, boolean isRetired) {

    public long durationInMinutes() {
        return Duration.between(
                LocalTime.MIN,
                LocalTime.parse(duration())
        ).toMinutes();
    }
}
