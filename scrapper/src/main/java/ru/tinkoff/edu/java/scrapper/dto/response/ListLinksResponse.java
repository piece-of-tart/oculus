package ru.tinkoff.edu.java.scrapper.dto.response;

import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, Integer size) {
}