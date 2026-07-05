DELETE FROM review_reactions;
DELETE FROM reviews;
DELETE FROM film_likes;
DELETE FROM user_events;
DELETE FROM film_genres;
DELETE FROM film_directors;
DELETE FROM films;
DELETE FROM mpa;
DELETE FROM genres;
DELETE FROM users;
DELETE FROM directors;

ALTER TABLE reviews ALTER COLUMN review_id RESTART WITH 1;
ALTER TABLE mpa ALTER COLUMN id RESTART WITH 1;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE films ALTER COLUMN id RESTART WITH 1;
ALTER TABLE genres ALTER COLUMN id RESTART WITH 1;
ALTER TABLE user_events ALTER COLUMN event_id RESTART WITH 1;
ALTER TABLE directors ALTER COLUMN id RESTART WITH 1;

INSERT INTO mpa (name) VALUES ('G');
INSERT INTO mpa (name) VALUES ('PG');
INSERT INTO mpa (name) VALUES ('PG-13');
INSERT INTO mpa (name) VALUES ('R');
INSERT INTO mpa (name) VALUES ('NC-17');

INSERT INTO genres (name) VALUES ('Комедия');
INSERT INTO genres (name) VALUES ('Драма');
INSERT INTO genres (name) VALUES ('Мультфильм');
INSERT INTO genres (name) VALUES ('Триллер');
INSERT INTO genres (name) VALUES ('Документальный');
INSERT INTO genres (name) VALUES ('Боевик');