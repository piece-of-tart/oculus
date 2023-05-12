package ru.tinkoff.edu.java.scrapper.dto.response;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

public record LinkUpdate(Long id, URI uri, String description, List<Long> tgChatIds) implements Serializable {
}
