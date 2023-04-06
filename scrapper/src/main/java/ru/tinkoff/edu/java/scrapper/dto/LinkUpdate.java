package ru.tinkoff.edu.java.scrapper.dto;

import java.net.URI;
import java.util.List;

public record LinkUpdate(Long id, URI uri, String description, List<Long> tgChatIds) {
}
