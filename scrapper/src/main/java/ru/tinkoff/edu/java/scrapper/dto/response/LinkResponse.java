package ru.tinkoff.edu.java.scrapper.dto.response;

import java.net.URI;

public record LinkResponse(Long tgChatId, URI uri) {
}
