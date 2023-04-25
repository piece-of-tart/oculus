package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.dto.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateData;

import java.net.URI;
import java.util.Collection;
import java.util.List;

public interface LinkService {
    LinkEntity add(LinkEntity linkEntity);
    LinkEntity remove(long tgChatId, URI uri);
    Collection<LinkEntity> listAll(long tgChatId);
    LinkEntity get(long chatId, URI uri);
    List<LinkEntity> get(URI uri);
    List<LinkUpdateData> getListByType(String typeName);
    void update(LinkUpdateData linkUpdateData);
}

