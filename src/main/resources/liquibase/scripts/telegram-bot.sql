-- liquibase formatted sql

-- changeset danil:1
create TABLE users(
    id serial PRIMARY KEY,
    chat_id varchar(200) NOT NULL unique
);

CREATE INDEX user_chat_id_index ON users(chat_id);


-- changeset danil:2
create TABLE tasks(
    id serial PRIMARY KEY,
    chat_id varchar(200) NOT NULL unique,
    date DATE NOT NULL,
    time TIME NOT NULL,
    text text NOT NULL,
    FOREIGN KEY (chat_id) REFERENCES users(chat_id)
);

-- changeset danil:3
DROP INDEX user_chat_id_index;

--changeset danil:4
ALTER TABLE tasks DROP CONSTRAINT tasks_chat_id_fkey;

--changeset danil:5
ALTER TABLE tasks ADD COLUMN user_id bigint;
ALTER TABLE tasks DROP COLUMN chat_id;

--changeset danil:6
ALTER table tasks ADD FOREIGN KEY (user_id) REFERENCES users(id);
