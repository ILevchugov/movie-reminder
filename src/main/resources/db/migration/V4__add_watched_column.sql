alter table chat_movie_list
    add column if not exists watched boolean not null default false