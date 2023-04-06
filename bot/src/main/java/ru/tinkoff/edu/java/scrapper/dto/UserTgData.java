package ru.tinkoff.edu.java.scrapper.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserTgData {
    @NonNull
    private UserDTO userDTO;
    private String lastSentCommand;
    private Integer commandStage;
}
