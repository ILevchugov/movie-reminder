create table if not exists movies
(
    id           text  not null primary key,
    title        text  not null,
    image        text,
    description  text,
    custom       boolean
);


create table if not exists chat_movie_list
(
    chat_id    bigint not null,
    movie_id   text   not null,
    constraint movie_id_foreign_key FOREIGN KEY (movie_id) REFERENCES movies (id),
    constraint chat_movie_unique UNIQUE(chat_id, movie_id)
);

