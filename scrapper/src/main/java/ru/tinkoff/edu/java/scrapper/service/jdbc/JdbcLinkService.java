package ru.tinkoff.edu.java.scrapper.service.jdbc;

import ru.tinkoff.edu.java.scrapper.dao.jdbc.JdbcLinkDao;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkUpdateData;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.URI;
import java.util.Collection;
import java.util.List;

public class JdbcLinkService implements LinkService {
    private final JdbcLinkDao jdbcLinkDao;

    public JdbcLinkService(JdbcLinkDao jdbcLinkDao) {
        this.jdbcLinkDao = jdbcLinkDao;
    }

    @Override
    public LinkEntity add(LinkEntity linkEntity) {
        jdbcLinkDao.add(linkEntity);
        return linkEntity;
    }

    @Override
    public LinkEntity remove(long tgChatId, URI uri) {
        return jdbcLinkDao.remove(tgChatId, uri);
    }

    @Override
    public Collection<LinkEntity> listAll(long tgChatId) {
        return jdbcLinkDao.findAll();
    }

    @Override
    public LinkEntity getLink(long chatId, URI uri) {
        return jdbcLinkDao.getLinkUsageForAllUsers(chatId, uri);
    }

    @Override
    public List<LinkEntity> getLinksByUri(URI uri) {
        return jdbcLinkDao.getLinkUsageForAllUsers(uri);
    }

    @Override
    public List<LinkUpdateData> getLinksByType(String typeName) {
        return jdbcLinkDao.findAllByType(typeName);
    }

    @Override
    public void update(LinkUpdateData linkUpdateData) {
        jdbcLinkDao.updateLinkDataInfo(linkUpdateData);
    }
}
