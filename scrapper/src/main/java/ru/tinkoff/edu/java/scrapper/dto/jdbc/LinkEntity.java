package ru.tinkoff.edu.java.scrapper.dto.jdbc;

import java.net.URI;
import java.sql.Date;

public record LinkEntity(URI uri, long chatId, String description, Long lastUpdatedId, Date lastChecked) {
}
