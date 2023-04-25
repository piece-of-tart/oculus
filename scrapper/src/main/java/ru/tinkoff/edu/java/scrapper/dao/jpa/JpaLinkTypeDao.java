package ru.tinkoff.edu.java.scrapper.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.dto.jpa.LinkType;

public interface JpaLinkTypeDao extends JpaRepository<LinkType, Long> {

    LinkType findLinkTypeByType(String type);
}
