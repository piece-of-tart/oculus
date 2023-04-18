package ru.tinkoff.edu.java.scrapper.dto;

import java.net.URI;
import java.sql.Date;

public record LinkUpdateData(URI uri, long lastUpdatedId, Date lastChecked) {
}
