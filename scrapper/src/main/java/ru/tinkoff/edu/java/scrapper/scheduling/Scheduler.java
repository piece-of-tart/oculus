package ru.tinkoff.edu.java.scrapper.scheduling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class Scheduler {

    private final Duration interval;

    public Scheduler(@Value("${scheduler.interval}") int interval) {
        this.interval = Duration.ofMillis(interval);
    }

    public Duration getInterval() {
        return interval;
    }
}