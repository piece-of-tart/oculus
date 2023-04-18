package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.jvnet.hk2.annotations.Service;
import ru.tinkoff.edu.java.scrapper.dao.JdbcLinkDao;
import ru.tinkoff.edu.java.scrapper.dto.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateData;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Service
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
    public LinkEntity get(long chatId, URI uri) {
        return jdbcLinkDao.get(chatId, uri);
    }

    @Override
    public List<LinkEntity> get(URI uri) {
        return jdbcLinkDao.get(uri);
    }

    @Override
    public List<LinkUpdateData> getListByType(String typeName) {
        return jdbcLinkDao.findAllByType(typeName);
    }

    @Override
    public void update(LinkUpdateData linkUpdateData) {
        jdbcLinkDao.updateLinkDataInfo(linkUpdateData);
    }
}
