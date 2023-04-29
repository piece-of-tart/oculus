package ru.tinkoff.edu.java.scrapper.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.dto.jpa.Link;
import ru.tinkoff.edu.java.scrapper.dto.jpa.LinkType;

import java.util.List;

public interface JpaLinkDao extends JpaRepository<Link, Long> {
    boolean existsByUri(String uri);
    Link findByUri(String uri);

    List<Link> findByType(LinkType type);
}
