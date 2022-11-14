create table category(
    id varchar(36) not null primary key,
    name varchar(255) not null,
    description varchar(4000),
    active boolean not null default true,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    deleted_at timestamp(6)
);