package ru.tinkoff.edu.java.scrapper.dao.jdbc;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkUpdateData;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class JdbcLinkDao {
    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ID_FROM_LINK_BY_URI = "SELECT id FROM link WHERE uri = ?";
    private static final String SELECT_ID_FROM_LINK_TYPE_BY_TYPE = "SELECT id FROM link_type WHERE type = ?";
    private static final String SELECT_ALL_LINK_ALL_CHAT_LINK_FROM_CHAT_LINK =
            "SELECT link.*, chat_link.* FROM chat_link ";

    private static final String DESCRIPTION_COL = "description";
    private static final String URI_COL = "uri";
    private static final String CHAT_ID_COL = "chat_id";
    private static final String LAST_CHECKED_COL = "last_checked";
    private static final String LAST_UPDATE_ID_COL = "last_updated_id";


    public JdbcLinkDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(LinkEntity linkEntity) {
        Optional<Long> linkTypeId = getTypeIdByUri(linkEntity.uri());
        if (linkTypeId.isEmpty()) {
            log.warn("Link with linkTypeId = null occurred in add to database stage. linkEntity.uri():"
                    + linkEntity.uri());
            throw new RuntimeException("(My exception): Unsupported link " + linkEntity.uri());
        }
        jdbcTemplate.update("INSERT INTO link (uri, last_updated_id, last_checked, type_id) "
                        + " VALUES (?, ?, ?, ?) ON CONFLICT (uri) DO NOTHING;", linkEntity.uri().toString(),
                linkEntity.lastUpdatedId(), linkEntity.lastChecked(), linkTypeId.get());
        long linkId = jdbcTemplate.queryForObject(SELECT_ID_FROM_LINK_BY_URI,
                Long.class, linkEntity.uri().toString());
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?) ON CONFLICT (id) DO NOTHING;", linkEntity.chatId());
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id, description) VALUES (?, ?, ?)",
                linkEntity.chatId(), linkId, linkEntity.description());
    }

    private Optional<Long> getTypeIdByUri(URI uri) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_ID_FROM_LINK_TYPE_BY_TYPE,
                Long.class, uri.getHost()));
    }

    public LinkEntity remove(long chatId, URI uri) {
        long linkId = jdbcTemplate.queryForObject(SELECT_ID_FROM_LINK_BY_URI, Long.class, uri.toString());
        return jdbcTemplate.query("DELETE FROM chat_link WHERE chat_id = ? AND link_id = ? RETURNING *",
                ps -> {
                    ps.setLong(1, chatId);
                    ps.setLong(2, linkId);
                },
                rs -> {
                    LinkEntity returnLinkEntity = null;
                    while (rs.next()) {
                        returnLinkEntity = new LinkEntity(uri, chatId, rs.getString(DESCRIPTION_COL),
                                -1L, null);
                    }
                    return returnLinkEntity;
                });
    }

    public LinkEntity getLinkUsageForAllUsers(long chatId, URI uri) {
        return jdbcTemplate.query(SELECT_ALL_LINK_ALL_CHAT_LINK_FROM_CHAT_LINK
                        + " RIGHT OUTER JOIN link ON link.id = chat_link.link_id WHERE chat_id = ? AND uri = ?;",
                ps -> {
                    ps.setLong(1, chatId);
                    ps.setString(2, uri.toString());
                },
                rs -> {
                    LinkEntity returnLinkEntity = null;
                    while (rs.next()) {
                        returnLinkEntity = new LinkEntity(uri, chatId, rs.getString(DESCRIPTION_COL),
                                rs.getLong(LAST_UPDATE_ID_COL), rs.getDate(LAST_CHECKED_COL));
                    }
                    return returnLinkEntity;
                });
    }

    public List<LinkEntity> getLinkUsageForAllUsers(URI uri) {
        return jdbcTemplate.query(SELECT_ALL_LINK_ALL_CHAT_LINK_FROM_CHAT_LINK
                        + " LEFT OUTER JOIN link ON chat_link.link_id = link.id WHERE link.uri = ?",
                ps -> {
                    ps.setString(1, uri.toString());
                },
                rse -> {
                    List<LinkEntity> linkEntities = new ArrayList<>();
                    while (rse.next()) {
                        linkEntities.add(new LinkEntity(uri,
                                rse.getLong(CHAT_ID_COL), rse.getString(DESCRIPTION_COL),
                                rse.getLong(LAST_UPDATE_ID_COL), rse.getDate(LAST_CHECKED_COL)));
                    }
                    return linkEntities;
                });
    }

    private Optional<Long> getLinkIdByUri(URI uri) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_ID_FROM_LINK_BY_URI,
                Long.class, uri.toString()));
    }

    public List<LinkEntity> findAll() {
        return jdbcTemplate.query("SELECT link.*, chat_link.* FROM link"
                + " RIGHT OUTER JOIN chat_link ON link.id = chat_link.link_id", rs -> {
            final List<LinkEntity> linkEntities = new ArrayList<>();
            while (rs.next()) {
                linkEntities.add(new LinkEntity(URI.create(rs.getString(URI_COL)),
                        rs.getLong(CHAT_ID_COL), rs.getString(DESCRIPTION_COL),
                        rs.getLong(LAST_UPDATE_ID_COL), rs.getDate(LAST_CHECKED_COL)));
            }
            return linkEntities;
        });
    }

    public List<LinkUpdateData> findAllByType(String typeName) {
        Long typeId = jdbcTemplate.query(SELECT_ID_FROM_LINK_TYPE_BY_TYPE,
                ps -> {
                    ps.setString(1, typeName);
                },
                rse -> {
                    if (!rse.next()) {
                        log.warn("Couldn't find type of link '" + typeName + "' which method "
                                + getClass().toString() + ".findAllByType" + " received");
                        return -1L;
                    }
                    return rse.getLong("id");
                });

        return jdbcTemplate.query("SELECT * FROM link WHERE type_id = ?",
                ps -> {
                    ps.setLong(1, typeId);
                },
                rs -> {
                    List<LinkUpdateData> linkUpdateData = new ArrayList<>();
                    while (rs.next()) {
                        linkUpdateData.add(new LinkUpdateData(URI.create(rs.getString(URI_COL)),
                                rs.getLong(LAST_UPDATE_ID_COL), rs.getDate(LAST_CHECKED_COL)));
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
