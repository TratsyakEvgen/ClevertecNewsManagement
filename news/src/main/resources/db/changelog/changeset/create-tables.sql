CREATE TABLE news
(
    news_id  BIGSERIAL PRIMARY KEY,
    username TEXT      NOT NULL,
    date     TIMESTAMP NOT NULL,
    title    TEXT      NOT NULL,
    text     TEXT      NOT NULL,
    text_tsv tsvector GENERATED ALWAYS AS (to_tsvector('english',
                                                       coalesce(username, '') || ' ' || coalesce(title, '') || ' ' ||
                                                       coalesce(text, ''))) STORED
);

CREATE TABLE comments
(
    comment_id BIGSERIAL PRIMARY KEY,
    username   TEXT      NOT NULL,
    date       TIMESTAMP NOT NULL,
    text       TEXT      NOT NULL,
    news_id    BIGINT    NOT NULL,
    text_tsv   tsvector GENERATED ALWAYS AS (to_tsvector('english',
                                                         coalesce(username, '') || ' ' ||
                                                         coalesce(text, ''))) STORED
);

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_news FOREIGN KEY (news_id) REFERENCES news (news_id);

CREATE INDEX comment_text_search_idx ON comments USING GIN (text_tsv);

CREATE INDEX news_text_search_idx ON news USING GIN (text_tsv);