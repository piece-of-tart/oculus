package ru.tinkoff.edu.java.scrapper.dto.jdbc;

import java.net.URI;
import java.sql.Date;

public record LinkUpdateData(URI uri, long lastUpdatedId, Date lastChecked) {
}
