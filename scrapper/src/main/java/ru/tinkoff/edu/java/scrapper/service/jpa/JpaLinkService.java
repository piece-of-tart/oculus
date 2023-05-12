package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dao.jpa.JpaChatLinkDao;
import ru.tinkoff.edu.java.scrapper.dao.jpa.JpaLinkDao;
import ru.tinkoff.edu.java.scrapper.dao.jpa.JpaLinkTypeDao;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkUpdateData;
import ru.tinkoff.edu.java.scrapper.dto.jpa.ChatLink;
import ru.tinkoff.edu.java.scrapper.dto.jpa.ChatLinkId;
import ru.tinkoff.edu.java.scrapper.dto.jpa.Link;
import ru.tinkoff.edu.java.scrapper.dto.jpa.LinkType;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.URI;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class JpaLinkService implements LinkService {
    private final JpaLinkDao jpaLinkDao;
    private final JpaChatLinkDao jpaChatLinkDao;
    private final JpaLinkTypeDao jpaLinkTypeDao;

    @Override
    @Transactional
    public LinkEntity add(LinkEntity linkEntity) {
        if (!jpaLinkDao.existsByUri(linkEntity.uri().toString())) {
            final Link link = new Link()
                    .setUri(linkEntity.uri().toString())
                    .setLastUpdatedId(-1L)
                    .setType(jpaLinkTypeDao.findLinkTypeByType(linkEntity.uri().getHost()));
            if (linkEntity.lastChecked() != null) {
                link.setLastChecked(Timestamp.valueOf(linkEntity.lastChecked().toLocalDate().atStartOfDay()));
            }
            log.warn("save:" + link);
            jpaLinkDao.save(link);
        }
        final Long linkId = jpaLinkDao.findByUri(linkEntity.uri().toString()).getId();
        jpaChatLinkDao.save(new ChatLink()
                .setChatId(linkEntity.chatId())
                .setLinkId(linkId)
                .setDescription(linkEntity.description()));
        return linkEntity;
    }

    @Override
    @Transactional
    public LinkEntity remove(long tgChatId, URI uri) {
        Link link = jpaLinkDao.findByUri(uri.toString());
        ChatLink chatLink = jpaChatLinkDao.findById(new ChatLinkId(tgChatId, link.getId())).orElseThrow(() ->
                new RuntimeException("Cannot remove row " + new ChatLinkId(tgChatId, link.getId()) + " because it doesn't exist."));
        jpaChatLinkDao.delete(chatLink);
        return new LinkEntity(uri, tgChatId, chatLink.getDescription(), link.getLastUpdatedId(),
                Date.valueOf(link.getLastChecked().toLocalDateTime().toLocalDate()));
    }

    @Override
    @Transactional
    public Collection<LinkEntity> listAll(long tgChatId) {
        final Map<Long, List<ChatLink>> chatLinkMap = getMapFromLinkIdToChatLinksByChatId(tgChatId);
        log.warn("getMapFromLinkIdToChatLinksByChatId: " + chatLinkMap);
        final List<LinkEntity> linkEntities = new ArrayList<>();
        for (var mapEntity : chatLinkMap.entrySet()) {
            Link link = jpaLinkDao.findById(mapEntity.getKey()).orElseThrow(() ->
                    new RuntimeException("Found link which keeps in chat_link table and doesn't keep in link table"));
            linkEntities.addAll(mapEntity.getValue().stream().map(e -> new LinkEntity(
                    URI.create(link.getUri()),
                    tgChatId,
                    e.getDescription(),
                    link.getLastUpdatedId(),
                    Date.valueOf(LocalDate.now()))
            ).toList());
        }
        return linkEntities;
    }


    private Map<Long, List<ChatLink>> getMapFromLinkIdToChatLinksByChatId(long tgChatId) {
        final List<ChatLink> chatLinks = jpaChatLinkDao.findAllByChatId(tgChatId);
        log.warn("findAllByChatId:" + chatLinks);
        final Map<Long, List<ChatLink>> chatLinkMap = new HashMap<>();
        for (var chatLink : chatLinks) {
            chatLinkMap.compute(chatLink.getLinkId(), (k, v) -> {
                if (v == null) {
                    List<ChatLink> res = new ArrayList<>();
                    res.add(chatLink);
                    return res;
                } else {
                    v.add(chatLink);
                    return v;
                }
            });
        }
        return chatLinkMap;
    }

        @Override
        @Transactional
        public LinkEntity getLink (long chatId, URI uri){
            final Link link = jpaLinkDao.findByUri(uri.toString());
            final ChatLink chatLink = jpaChatLinkDao.findById(
                    new ChatLinkId().setChatId(chatId).setLinkId(link.getId())).orElseThrow();
            log.warn("GET_LINK: " + link.getLastChecked().toLocalDateTime().toLocalDate());
            return new LinkEntity(uri, chatId, chatLink.getDescription(), link.getLastUpdatedId(),
                    Date.valueOf(link.getLastChecked().toLocalDateTime().toLocalDate()));
        }

        @Override
        @Transactional
        public List<LinkEntity> getLinksByUri (URI uri){
            final Link link = jpaLinkDao.findByUri(uri.toString());
            final List<ChatLink> chatLinks = jpaChatLinkDao.findAllByLinkId(link.getId());
            return chatLinks.stream().map(chatLink -> new LinkEntity(uri, chatLink.getChatId(),
                    chatLink.getDescription(), link.getLastUpdatedId(),
                    Date.valueOf(link.getLastChecked() == null ? LocalDate.now() :
                            link.getLastChecked().toLocalDateTime().toLocalDate()))
            ).toList();
        }

        @Override
        @Transactional
        public List<LinkUpdateData> getLinksByType (String typeName) {
            final LinkType linkType = jpaLinkTypeDao.findLinkTypeByType(typeName);
            final List<Link> links = jpaLinkDao.findByType(linkType);
            return links.stream().map(link ->
                    new LinkUpdateData(URI.create(link.getUri()), link.getLastUpdatedId(),
                            Date.valueOf(link.getLastChecked() == null ? LocalDate.now() :
                                    link.getLastChecked().toLocalDateTime().toLocalDate()))
            ).toList();
        }

        @Override
        @Transactional
        public void update (LinkUpdateData linkUpdateData) {
            final Link link = jpaLinkDao.findByUri(linkUpdateData.uri().toString());
            link.setLastUpdatedId(linkUpdateData.lastUpdatedId())
                    .setLastChecked(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Europe/Moscow"))));
            jpaLinkDao.save(link);
        }
    }
