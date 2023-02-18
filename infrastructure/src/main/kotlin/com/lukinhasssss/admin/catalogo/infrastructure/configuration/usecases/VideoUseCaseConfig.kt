package com.lukinhasssss.admin.catalogo.infrastructure.configuration.usecases

import com.lukinhasssss.admin.catalogo.application.video.media.update.DefaultUpdateMediaStatusUseCase
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VideoUseCaseConfig(
    private val videoGateway: VideoGateway
) {

    @Bean
    fun updateMediaStatusUseCase() = DefaultUpdateMediaStatusUseCase(videoGateway)
}
