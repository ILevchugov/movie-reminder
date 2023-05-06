alter table chat_movie_list
    add column if not exists recently_picked boolean not null default false