package ru.tinkoff.edu.java.scrapper.dto;

import java.util.List;

public record UserDTO(String username, List<LinkDTO> trackedLinks) {
}
