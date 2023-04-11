package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination

interface VideoGateway {

    fun create(aVideo: Video): Video

    fun findById(anID: VideoID): Video?

    fun findAll(aQuery: VideoSearchQuery): Pagination<VideoPreview>

    fun update(aVideo: Video): Video

    fun deleteById(anID: VideoID)
}
