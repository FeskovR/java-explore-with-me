DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE IF NOT EXISTS hits (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    uri VARCHAR NOT NULL,
    ip VARCHAR(50) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    app VARCHAR NOT NULL
);