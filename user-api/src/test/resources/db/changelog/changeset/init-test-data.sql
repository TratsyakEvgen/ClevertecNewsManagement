CREATE TABLE users
(
    user_id  BIGSERIAL PRIMARY KEY,
    username TEXT   NOT NULL UNIQUE,
    password TEXT   NOT NULL,
    role_id  BIGINT NOT NULL
);

CREATE TABLE roles
(
    role_id   BIGSERIAL PRIMARY KEY,
    role_name TEXT NOT NULL UNIQUE
);

ALTER TABLE users
    ADD CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES roles (role_id);

CREATE INDEX users_username_idx ON users(username);

CREATE INDEX roles_role_name_idx ON roles(role_name);

INSERT INTO roles(role_name)
VALUES ('ADMIN'),
       ('JOURNALIST'),
       ('SUBSCRIBER');

INSERT INTO users(username, password, role_id)
VALUES ('user1', '$2a$10$ncApYSLqk6bhjD8pH6KwmuFYESWEcR8Uuy.X5g7Qbb7p/AeKFI7XK', 3),
       ('user2', '$2a$10$fkxn35G0GHe/By7hp6g39eqwI1LT5gx3VsF25D1IIl/P63HEjT.ne', 3),
       ('user3', '$2a$10$qNlRbxXjDhA/fwCD7/Lswet1BS/t2JyxyFd6w4DlidUJvY6IoBscq', 3),
       ('user4', '$2a$10$LXm8/89M4yhm.vyUoSbD8OZEk4ifnfjcUCmH00q76ACeUHYH0EZ5S', 3),
       ('user5', '$2a$10$nleFAvm79qvgsxXEhqQVfutRJbkGiZUF/kmYl/FxxsfqQiXlDpGHy', 3),
       ('user6', '$2a$10$GNttqcfqdmU3TadjGlr68uBZGzRjaVbRInJsZ64gcGFovp.I4z3IK', 3),
       ('user7', '$2a$10$p/IgNmJ9KIBKRpYuuElY.OH.Mgc0Sehs2EQ3Xfavb9yN4xN6skwky', 3),
       ('user8', '$2a$10$78M5XF8aaln2o9jKxKOZieNrKhaz8NUVJ9PsMqyaoxbotqeDaQ4vu', 3),
       ('user9', '$2a$10$YGSbOUOxWMOfb3wTYWYWsuIFEhR6OOftp5Eu/W03uQNlLNQYBujq6', 3),
       ('user10', '$2a$10$Ft80YA.dBtohcR5Zo/x5zu5rsc3SiAnHnDDeHJdLK8vKduxb6fgni', 3),
       ('admin1', '$2a$10$JaxMTCL1EiHXpE8sRzxlSua1Ytnc1b7Fi3.r3FaRFPHjkCg0k40oS', 1),
       ('journalist1', '$2a$10$taUmLDjqkrzl1qXHcqUfJeGpgQZFnVFxgiMznDv2AwjYakU1AipFO', 2),
       ('journalist2', '$2a$10$QIZfsBE87s3vxJcseIjTVOc4XYgxvEvu07lyEs4gn6Flr9GJbrh2y', 2),
       ('journalist3', '$2a$10$BPsPLzMtYfh/BEe0uE3uD.G3w9Q3PT5fV8GOxHgfQEMAsa96YMje.', 2),
       ('journalist4', '$2a$10$6Ce3tcOK2Go58bpWYjbbF.Q7vxVdAIXWRiHUu.aDajpRpLDl53B1K', 2)
