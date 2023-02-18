package com.lukinhasssss.admin.catalogo.infrastructure.amqp

import com.lukinhasssss.admin.catalogo.application.video.media.update.UpdateMediaStatusCommand
import com.lukinhasssss.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.COMPLETED
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.json.Json
import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoEncoderCompleted
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoEncoderError
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoEncoderResult
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class VideoEncoderListener(
    private val updateMediaStatusUseCase: UpdateMediaStatusUseCase
) {

    companion object {
        const val LISTENER_ID = "videoEncodedListener"
    }

    @RabbitListener(id = LISTENER_ID, queues = ["\${amqp.queues.video-encoded.queue}"])
    fun onVideoEncodedMessage(@Payload message: String) {
        when (val aResult = Json.readValue(message, VideoEncoderResult::class.java)) {
            is VideoEncoderCompleted -> {
                Logger.info(
                    message = "Video successfully encoded",
                    payload = message
                )

                val aCommand = UpdateMediaStatusCommand(
                    videoId = aResult.id,
                    status = COMPLETED,
                    resourceId = aResult.video.resourceId,
                    folder = aResult.video.encodedVideoFolder,
                    filename = aResult.video.filePath
                )

                updateMediaStatusUseCase.execute(aCommand)
            }

            is VideoEncoderError -> Logger.error(
                message = "Error when encoding video",
                payload = message
            )
        }
    }
}
