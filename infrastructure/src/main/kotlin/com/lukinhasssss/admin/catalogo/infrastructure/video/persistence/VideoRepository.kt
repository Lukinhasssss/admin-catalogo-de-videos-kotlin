package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.video.VideoPreview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface VideoRepository : JpaRepository<VideoJpaEntity, String> {

    @Query(
        """
        select new com.lukinhasssss.admin.catalogo.domain.video.VideoPreview(
            video.id,
            video.title,
            video.description,
            video.createdAt,
            video.updatedAt
        )
        from Video video
            join video.castMembers members
            join video.categories categories
            join video.genres genres
        where
            ( upper(video.title) like :terms)
        and
            ( members.id.castMemberId in :castMembers )
        and
            ( categories.id.categoryId in :categories )
        and
            ( genres.id.genreId in :genres )
    """
    )
    fun findAll(
        @Param("terms") terms: String,
        @Param("castMembers") castMembers: Set<String>,
        @Param("categories") categories: Set<String>,
        @Param("genres") genres: Set<String>,
        page: Pageable
    ): Page<VideoPreview>
}

/** TODO: Verificar essas queries depois pq pode dar merda
 * where
 *   ( :terms is null or upper(video.title) like :terms)
 * and
 *   ( :castMembers is null or members.id.castMemberId in :castMembers )
 * and
 *   ( :categories is null or categories.id.categoryId in :categories )
 * and
 *   ( :genres is null or genres.id.genreId in :genres )
 */
