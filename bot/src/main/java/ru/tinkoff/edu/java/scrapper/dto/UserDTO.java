package ru.tinkoff.edu.java.scrapper.dto;

import ru.tinkoff.edu.java.scrapper.dto.LinkDTO;

import java.util.List;

public record UserDTO(String username, List<LinkDTO> trackedLinks) {
}
