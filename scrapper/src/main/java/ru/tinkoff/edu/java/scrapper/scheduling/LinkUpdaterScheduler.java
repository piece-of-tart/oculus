package ru.tinkoff.edu.java.scrapper.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class LinkUpdaterScheduler {
    private static final Logger logger = LoggerFactory.getLogger(LinkUpdaterScheduler.class);

    @Scheduled(fixedDelayString = "#{scheduler.getInterval()}")
    public void update() {
        logger.info("Update " + Calendar.getInstance().toString());
    }
}
