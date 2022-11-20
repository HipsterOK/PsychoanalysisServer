CREATE DATABASE IF NOT EXISTS dev;
USE dev;
CREATE TABLE question
(
  id   SERIAL NOT NULL,
  text varchar NOT NULL UNIQUE
);
INSERT INTO question(text) VALUES ('Часто ли1');
INSERT INTO question(text) VALUES ('Часто ли2');