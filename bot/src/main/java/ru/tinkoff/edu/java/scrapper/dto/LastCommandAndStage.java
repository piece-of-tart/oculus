package ru.tinkoff.edu.java.scrapper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class LastCommandAndStage {
    private String lastCommand;
    private Integer stage; // Now we have only 1/2-stage commands, but what if we'll have to use more stages
}
