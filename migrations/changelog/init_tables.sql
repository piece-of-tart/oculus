CREATE TABLE IF NOT EXISTS chat (
    id SERIAL PRIMARY KEY NOT NULL,
    tg_chat_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS link (
    id SERIAL PRIMARY KEY,
    uri VARCHAR(255) NOT NULL,
    description TEXT,
    chat_id BIGINT REFERENCES chat(id)
);

CREATE TABLE IF NOT EXISTS chat_link (
    chat_link_id SERIAL PRIMARY KEY NOT NULL,
    chat_id INTEGER,
    link_id INTEGER,
    FOREIGN KEY (chat_id) REFERENCES chat(id),
    FOREIGN KEY (link_id) REFERENCES link(id)
);