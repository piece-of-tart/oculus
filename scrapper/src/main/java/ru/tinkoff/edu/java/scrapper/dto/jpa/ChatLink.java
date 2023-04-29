package ru.tinkoff.edu.java.scrapper.dto.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "chat_link")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ChatLinkId.class)
public class ChatLink {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Id
    @Column(name = "link_id")
    private Long linkId;

    @Column(name = "description")
    private String description;
}

