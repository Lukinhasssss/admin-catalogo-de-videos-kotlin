package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.video.VideoPreview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface VideoRepository : JpaRepository<VideoJpaEntity, String> {

    @Query(
        value = """
        select new com.lukinhasssss.admin.catalogo.domain.video.VideoPreview(
            video.id as id,
            video.title as title,
            video.description as description,
            video.createdAt as createdAt,
            video.updatedAt as updatedAt
        )
        from Video video
            join video.castMembers members
            join video.categories categories
            join video.genres genres
        where
            ( :terms is empty or upper(video.title) like :terms)
        and
            ( :castMembers is empty or members.id.castMemberId in :castMembers )
        and
            ( :categories is empty or categories.id.categoryId in :categories )
        and
            ( :genres is empty or genres.id.genreId in :genres )
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
