create table genres(
    id varchar(36) not null primary key,
    name varchar(255) not null,
    active boolean not null default true,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    deleted_at timestamp(6) null
);

create table genres_categories(
    genre_id varchar(36) not null,
    category_id varchar(36) not null,

    constraint idx_genre_category unique (genre_id, category_id),
    constraint fk_genre_id foreign key (genre_id) references genres (id) on delete cascade,
    constraint fk_category_id foreign key (category_id) references category (id) on delete cascade
)