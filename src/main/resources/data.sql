DELETE FROM review_reactions;
DELETE FROM film_directors;
DELETE FROM film_genres;
DELETE FROM film_likes;
DELETE FROM reviews;
DELETE FROM user_friendships;
DELETE FROM films;
DELETE FROM directors;
DELETE FROM mpa;
DELETE FROM genres;
DELETE FROM users;

ALTER TABLE directors ALTER COLUMN id RESTART WITH 1;
ALTER TABLE mpa ALTER COLUMN id RESTART WITH 1;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE films ALTER COLUMN id RESTART WITH 1;
ALTER TABLE genres ALTER COLUMN id RESTART WITH 1;

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