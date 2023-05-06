package ru.tinkoff.edu.java.scrapper.dto.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "link")
public class Link {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uri")
    private String uri;

    @Column(name = "last_updated_id")
    private Long lastUpdatedId;

    @Column(name = "last_checked", nullable = false)
    private Timestamp lastChecked = Timestamp.valueOf(LocalDateTime.now());

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private LinkType type;
}
