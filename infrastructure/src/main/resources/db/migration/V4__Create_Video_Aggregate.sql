create table videos_video_media(
    id char(32) not null primary key,
    name varchar(255) not null,
    checksum varchar(255) not null,
    file_path varchar(500) not null,
    encoded_path varchar(500) not null,
    media_status varchar(50) not null
);

create table videos_image_media(
    id char(32) not null primary key,
    name varchar(255) not null,
    checksum varchar(255) not null,
    file_path varchar(500) not null
);

create table videos(
    id char(32) not null primary key,
    title varchar(255) not null,
    description varchar(1000),
    year_launched smallint not null,
    opened boolean not null default false,
    published boolean not null default false,
    rating varchar(5) not null,
    duration decimal(5, 2) not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    trailer_id char(32) null,
    video_id char(32) null,
    banner_id char(32) null,
    thumbnail_id char(32) null,
    thumbnail_half_id char(32) null,

    constraint fk_v_trailer_id foreign key (trailer_id) references videos_video_media (id) on delete cascade,
    constraint fk_v_video_id foreign key (video_id) references videos_video_media (id) on delete cascade,
    constraint fk_v_banner_id foreign key (banner_id) references videos_image_media (id) on delete cascade,
    constraint fk_v_thumb_id foreign key (thumbnail_id) references videos_image_media (id) on delete cascade,
    constraint fk_v_thumb_half_id foreign key (thumbnail_half_id) references videos_image_media (id) on delete cascade
);

create table videos_categories(
    video_id char(32) not null,
    category_id char(32) not null,

    constraint idx_vcs_video_category unique (video_id, category_id),
    constraint fk_vcs_video_id foreign key (video_id) references videos (id),
    constraint fk_vcs_category_id foreign key (category_id) references category (id)
);

create table videos_genres(
    video_id char(32) not null,
    genre_id char(32) not null,

    constraint idx_vgs_video_genre unique (video_id, genre_id),
    constraint fk_vgs_video_id foreign key (video_id) references videos (id),
    constraint fk_vgs_genre_id foreign key (genre_id) references genres (id)
);

create table videos_cast_members(
    video_id char(32) not null,
    cast_member_id char(32) not null,

    constraint idx_vcms_video_member unique (video_id, cast_member_id),
    constraint fk_vcms_video_id foreign key (video_id) references videos (id),
    constraint fk_vcms_cast_member_id foreign key (cast_member_id) references cast_members (id)
);