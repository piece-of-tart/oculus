package ru.tinkoff.edu.java.scrapper.dto.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "chat")
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @Column(name = "id")
    private Long id;
}
