CREATE TABLE chat (
    id BIGINT PRIMARY KEY NOT NULL
);

CREATE TABLE IF NOT EXISTS link (
    id BIGSERIAL PRIMARY KEY,
    uri VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS chat_link (
    chat_id BIGINT,
    link_id BIGINT,
    description TEXT,
    PRIMARY KEY(chat_id, link_id),
    FOREIGN KEY (chat_id) REFERENCES chat(id),
    FOREIGN KEY (link_id) REFERENCES link(id)
);

CREATE INDEX IF NOT EXISTS idx_uri ON link(uri);