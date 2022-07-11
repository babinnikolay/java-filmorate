CREATE TABLE IF NOT EXISTS
    film
(
    film_id      INTEGER PRIMARY KEY,
    name         VARCHAR(100),
    description  VARCHAR(200),
    release_date DATETIME,
    duration     INTEGER, --ofMinutes
    mpa_id       INTEGER
);

CREATE TABLE IF NOT EXISTS
    filmorate_user
(
    user_id  INTEGER PRIMARY KEY,
    email    VARCHAR(100),
    login    VARCHAR(100),
    name     VARCHAR(200),
    birthday DATETIME
);

CREATE TABLE IF NOT EXISTS
    film_genre
(
    film_id  INTEGER,
    genre_id INTEGER,
    PRIMARY KEY (film_id, genre_id)
);

ALTER TABLE
    film_genre
    ADD FOREIGN KEY (film_id)
        REFERENCES film (film_id);

CREATE TABLE IF NOT EXISTS
    film_like
(
    film_id INTEGER,
    user_id INTEGER,
    PRIMARY KEY (film_id, user_id)
);

ALTER TABLE
    film_like
    ADD FOREIGN KEY (film_id)
        REFERENCES film (film_id);

ALTER TABLE
    film_like
    ADD FOREIGN KEY (user_id)
        REFERENCES filmorate_user (user_id);

CREATE TABLE IF NOT EXISTS
    user_friend
(
    user_id          INTEGER,
    friend_id        INTEGER,
    friend_status_id INTEGER,
    PRIMARY KEY (user_id, friend_id)
);

ALTER TABLE
    user_friend
    ADD FOREIGN KEY (user_id)
        REFERENCES filmorate_user (user_id);

ALTER TABLE
    user_friend
    ADD FOREIGN KEY (friend_id)
        REFERENCES filmorate_user (user_id);