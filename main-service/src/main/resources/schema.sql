DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS complition_events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;



CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT UQ_USER_NAME UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat REAL NOT NULL,
    lon REAL NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS events (
     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     annotation VARCHAR NOT NULL,
     title VARCHAR NOT NULL,
     confirmed_requests INTEGER,
     description VARCHAR NOT NULL,
     event_date TIMESTAMP WITHOUT TIME ZONE,
     created_on TIMESTAMP WITHOUT TIME ZONE,
     published_on TIMESTAMP WITHOUT TIME ZONE,
     paid BOOLEAN NOT NULL,
     request_moderation BOOLEAN NOT NULL,
     participant_limit INTEGER NOT NULL,
     status Smallint NOT NULL,
     category_id INTEGER,
     initiator_id INTEGER,
     location_id INTEGER,
     FOREIGN KEY (category_id) REFERENCES categories (id),
     FOREIGN KEY (location_id) REFERENCES locations (id),
     FOREIGN KEY (initiator_id) REFERENCES users (id),
     CONSTRAINT pk_event PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN NOT NULL,
    title VARCHAR NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
    );



CREATE TABLE IF NOT EXISTS compilation_events (
    event_id INTEGER NOT NULL,
    compilation_id INTEGER NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events (id),
    FOREIGN KEY (compilation_id) REFERENCES compilations (id)
    );

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    status Smallint NOT NULL,
    event_id INTEGER NOT NULL,
    requester_id INTEGER NOT NULL,
    FOREIGN KEY (requester_id) REFERENCES users (id),
    FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT pk_request PRIMARY KEY (id)
    );

