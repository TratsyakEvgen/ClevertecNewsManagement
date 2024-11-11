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

CREATE INDEX users_username_idx ON users (username);

CREATE INDEX roles_role_name_idx ON roles (role_name);
