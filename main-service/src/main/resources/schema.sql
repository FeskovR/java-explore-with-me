DROP TABLE IF EXISTS categories CASCADE;

CREATE TABLE IF NOT EXISTS categories(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(200) UNIQUE NOT NULL
);