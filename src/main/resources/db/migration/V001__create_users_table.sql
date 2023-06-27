DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY,
    user_id  uuid DEFAULT gen_random_uuid(),
    login    varchar(40)  NOT NULL,
    password varchar(100) NOT NULL,
    type     varchar(40)    NOT NULL,
    active   bool DEFAULT true
);
