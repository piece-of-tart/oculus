package ru.tinkoff.edu.java.scrapper.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.dto.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateData;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcLinkDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLinkDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(LinkEntity linkEntity) {
        jdbcTemplate.update("INSERT INTO link (uri, last_updated_id, last_checked)" +
                " VALUES (?, ?, ?) ON CONFLICT (uri) DO NOTHING;", linkEntity.uri().toString(), linkEntity.lastUpdatedId(),
                linkEntity.lastChecked());
        long linkId = jdbcTemplate.queryForObject("SELECT id FROM link WHERE uri = ?", Long.class, linkEntity.uri().toString());
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?) ON CONFLICT (id) DO NOTHING;", linkEntity.chatId());
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id, description) VALUES (?, ?, ?)",
                linkEntity.chatId(), linkId, linkEntity.description());
    }

    public LinkEntity remove(long chatId, URI uri) {
        long linkId = jdbcTemplate.queryForObject("SELECT id FROM link WHERE uri = ?", Long.class, uri.toString());
        return jdbcTemplate.query("DELETE FROM chat_link WHERE chat_id = ? AND link_id = ? RETURNING *",
                ps -> { ps.setLong(1, chatId); ps.setLong(2, linkId); },
                rs -> {
                    LinkEntity returnLinkEntity = null;
                    while (rs.next()) {
                        returnLinkEntity = new LinkEntity(uri, chatId, rs.getString("description"),
                                -1L, null);
                    }
                    return returnLinkEntity;
                });
    }

    public LinkEntity get(long chatId, URI uri) {
        return jdbcTemplate.query("SELECT link.*, chat_link.* FROM chat_link " +
                " RIGHT OUTER JOIN link ON link.id = chat_link.link_id WHERE chat_id = ? AND uri = ?;",
            ps -> { ps.setLong(1, chatId); ps.setString(2, uri.toString()); },
            rs -> {
                LinkEntity returnLinkEntity = null;
                while (rs.next()) {
                    returnLinkEntity = new LinkEntity(uri, chatId, rs.getString("description"),
                            rs.getLong("last_updated_id"), rs.getDate("last_checked"));
                }
                return returnLinkEntity;
            });
    }

    public List<LinkEntity> get(URI uri) {
        return jdbcTemplate.query("SELECT * FROM chat_link WHERE uri = ?",
            ps -> { ps.setString(1, uri.toString()); },
            rse -> {
                List<LinkEntity> linkEntities = new ArrayList<>();
                while (rse.next()) {
                    linkEntities.add(new LinkEntity(uri,
                            rse.getLong("chat_id"), rse.getString("description"),
                            rse.getLong("last_updated_id"), rse.getDate("last_checked")));
                }
                return linkEntities;
            });
    }

    public List<LinkEntity> findAll() {
        return jdbcTemplate.query("SELECT link.*, chat_link.* FROM link" +
                " RIGHT OUTER JOIN chat_link ON link.id = chat_link.link_id", rs -> {
            final List<LinkEntity> linkEntities = new ArrayList<>();
            while (rs.next()) {
                linkEntities.add(new LinkEntity(URI.create(rs.getString("uri")),
                        rs.getLong("chat_id"), rs.getString("description"),
                        rs.getLong("last_updated_id"), rs.getDate("last_checked")));
            }
            return linkEntities;
        });
    }

    public List<LinkUpdateData> findAllByType(String typeName) {
        long typeId = jdbcTemplate.queryForObject("SELECT id FROM link_type WHERE type = ?", Long.class, typeName);
        return jdbcTemplate.query("SELECT * FROM link WHERE type_id = ?",
                ps -> { ps.setLong(1, typeId); },
                rs -> {
                    List<LinkUpdateData> linkUpdateData = new ArrayList<>();
                    while (rs.next()) {
                        linkUpdateData.add(new LinkUpdateData(URI.create(rs.getString("uri")),
                                rs.getLong("last_updated_id"), rs.getDate("last_checked")));
                    }
                    return linkUpdateData;
                });
    }

    public void updateLinkDataInfo(LinkUpdateData linkUpdateData) {
        jdbcTemplate.update("UPDATE link SET uri = ?, last_updated_id = ?, last_checked = ?",
                linkUpdateData.uri().toString(),
                linkUpdateData.lastUpdatedId(),
                linkUpdateData.lastChecked());
    }
}
