create table cast_members(
    id char(32) not null primary key,
    name varchar(255) not null,
    type varchar(32) not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null
);