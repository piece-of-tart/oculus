package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkUpdateData;

import java.net.URI;
import java.util.Collection;
import java.util.List;

public interface LinkService {
    LinkEntity add(LinkEntity linkEntity);

    LinkEntity remove(long tgChatId, URI uri);

    Collection<LinkEntity> listAll(long tgChatId);

    LinkEntity getLink(long chatId, URI uri);

    List<LinkEntity> getLinksByUri(URI uri);

    List<LinkUpdateData> getLinksByType(String typeName);

    void update(LinkUpdateData linkUpdateData);
}

