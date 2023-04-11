package com.lukinhasssss.admin.catalogo.infrastructure.configuration.usecases

import com.lukinhasssss.admin.catalogo.application.video.create.DefaultCreateVideoUseCase
import com.lukinhasssss.admin.catalogo.application.video.delete.DefaultDeleteVideoUseCase
import com.lukinhasssss.admin.catalogo.application.video.media.get.DefaultGetMediaUseCase
import com.lukinhasssss.admin.catalogo.application.video.media.update.DefaultUpdateMediaStatusUseCase
import com.lukinhasssss.admin.catalogo.application.video.media.upload.DefaultUploadMediaUseCase
import com.lukinhasssss.admin.catalogo.application.video.retrieve.get.DefaultGetVideoByIdUseCase
import com.lukinhasssss.admin.catalogo.application.video.retrieve.list.DefaultListVideosUseCase
import com.lukinhasssss.admin.catalogo.application.video.update.DefaultUpdateVideoUseCase
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.video.MediaResourceGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VideoUseCaseConfig(
    private val categoryGateway: CategoryGateway,
    private val genreGateway: GenreGateway,
    private val castMemberGateway: CastMemberGateway,
    private val mediaResourceGateway: MediaResourceGateway,
    private val videoGateway: VideoGateway
) {

    @Bean
    fun createVideoUseCase() = DefaultCreateVideoUseCase(
        categoryGateway = categoryGateway,
        genreGateway = genreGateway,
        castMemberGateway = castMemberGateway,
        mediaResourceGateway = mediaResourceGateway,
        videoGateway = videoGateway
    )

    @Bean
    fun deleteVideoUseCase() = DefaultDeleteVideoUseCase(
        mediaResourceGateway = mediaResourceGateway, videoGateway = videoGateway
    )

    @Bean
    fun getMediaUseCase() = DefaultGetMediaUseCase(mediaResourceGateway)

    @Bean
    fun getVideoById() = DefaultGetVideoByIdUseCase(videoGateway)

    @Bean
    fun listVideosUseCase() = DefaultListVideosUseCase(videoGateway)

    @Bean
    fun updateMediaStatusUseCase() = DefaultUpdateMediaStatusUseCase(videoGateway)

    @Bean
    fun updateVideoUseCase() = DefaultUpdateVideoUseCase(
        categoryGateway = categoryGateway,
        genreGateway = genreGateway,
        castMemberGateway = castMemberGateway,
        mediaResourceGateway = mediaResourceGateway,
        videoGateway = videoGateway
    )

    @Bean
    fun uploadMediaUseCase() = DefaultUploadMediaUseCase(
        videoGateway = videoGateway, mediaResourceGateway = mediaResourceGateway
    )
}
