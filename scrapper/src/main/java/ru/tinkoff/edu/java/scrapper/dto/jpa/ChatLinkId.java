package ru.tinkoff.edu.java.scrapper.dto.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChatLinkId implements Serializable {
    private Long chatId;
    private Long linkId;
}
