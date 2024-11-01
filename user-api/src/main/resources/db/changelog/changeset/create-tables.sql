CREATE TABLE users
(
    user_id  BIGSERIAL PRIMARY KEY,
    username TEXT   NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role_id  BIGINT NOT NULL
);

CREATE TABLE roles
(
    role_id   BIGSERIAL PRIMARY KEY,
    role_name TEXT NOT NULL UNIQUE
);

ALTER TABLE users
    ADD CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES roles(role_id);
