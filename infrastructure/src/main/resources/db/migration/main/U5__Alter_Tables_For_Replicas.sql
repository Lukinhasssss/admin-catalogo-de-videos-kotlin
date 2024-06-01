alter table categories REPLICA IDENTITY DEFAULT;

alter table genres REPLICA IDENTITY DEFAULT;
alter table genres_categories REPLICA IDENTITY DEFAULT;

alter table cast_members REPLICA IDENTITY DEFAULT;

alter table videos_video_media REPLICA IDENTITY DEFAULT;
alter table videos_image_media REPLICA IDENTITY DEFAULT;
alter table videos REPLICA IDENTITY DEFAULT;
alter table videos_categories REPLICA IDENTITY DEFAULT;
alter table videos_genres REPLICA IDENTITY DEFAULT;
alter table videos_cast_members REPLICA IDENTITY DEFAULT;