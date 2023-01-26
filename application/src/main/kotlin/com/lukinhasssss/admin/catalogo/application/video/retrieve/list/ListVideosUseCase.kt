package com.lukinhasssss.admin.catalogo.application.video.retrieve.list

import com.lukinhasssss.admin.catalogo.application.UseCase
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.video.VideoSearchQuery

abstract class ListVideosUseCase : UseCase<VideoSearchQuery, Pagination<VideoListOutput>>()
