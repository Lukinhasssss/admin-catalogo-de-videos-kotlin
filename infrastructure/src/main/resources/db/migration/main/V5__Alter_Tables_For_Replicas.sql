alter table categories REPLICA IDENTITY FULL;

alter table genres REPLICA IDENTITY FULL;
alter table genres_categories REPLICA IDENTITY FULL;

alter table cast_members REPLICA IDENTITY FULL;

alter table videos_video_media REPLICA IDENTITY FULL;
alter table videos_image_media REPLICA IDENTITY FULL;
alter table videos REPLICA IDENTITY FULL;
alter table videos_categories REPLICA IDENTITY FULL;
alter table videos_genres REPLICA IDENTITY FULL;
alter table videos_cast_members REPLICA IDENTITY FULL;

-- O PostgreSQL não pode deletar linhas de uma tabela que está sendo replicada, a menos que a tabela tenha uma "identidade de réplica" definida.
-- A identidade de réplica é usada para identificar unicamente as linhas para replicação.
-- Você pode definir a identidade de réplica para a tabela usando o comando ALTER TABLE.
-- O Postgres não permite alterar a tabela para REPLICA IDENTITY FULL se a tabela não tiver uma chave primária.