package ru.tinkoff.edu.java.scrapper.dto;

import java.net.URI;
import java.sql.Date;

public record LinkEntity(URI uri, long chatId, String description, long lastUpdatedId, Date lastChecked) {
}
