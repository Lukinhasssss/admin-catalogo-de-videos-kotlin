package com.lukinhasssss.admin.catalogo.infrastructure.amqp

import com.lukinhasssss.admin.catalogo.AmqpTest
import com.lukinhasssss.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.json.Json
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoEncoderCompleted
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoEncoderError
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoMessage
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.VideoMetadata
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness
import org.springframework.amqp.rabbit.test.TestRabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@AmqpTest
class VideoEncoderListenerTest {

    @Autowired
    private lateinit var rabbitTemplate: TestRabbitTemplate

    @Autowired
    private lateinit var harness: RabbitListenerTestHarness

    @MockkBean
    private lateinit var updateMediaStatusUseCase: UpdateMediaStatusUseCase

    @Autowired
    @VideoEncodedQueue
    private lateinit var queueProperties: QueueProperties

    @Test
    fun givenErrorResult_whenCallsListener_shouldProcess() {
        // given
        val expectedError = VideoEncoderError(
            message = VideoMessage("123", "abc"),
            error = "Video not found"
        )
        val expectedMessage = Json.writeValueAsString(expectedError)

        // when
        rabbitTemplate.convertAndSend(queueProperties.queue, expectedMessage)

        // then
        val invocationData = harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS)

        with(invocationData) {
            assertNotNull(this)
            assertNotNull(arguments)

            val actualMessage = arguments[0] as String

            assertEquals(expectedMessage, actualMessage)
        }
    }

    @Test
    fun givenCompletedResult_whenCallsListener_shouldCallUseCase() {
        // given
        val expectedId = IdUtils.uuid()
        val expectedOutputBucket = "lukinhastest"
        val expectedStatus = MediaStatus.COMPLETED
        val expectedEncoderVideoFolder = "anyfolder"
        val expectedResourceId = IdUtils.uuid()
        val expectedFilePath = "any.mp4"
        val expectedMetadata = VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath)
        val aResult = VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata)
        val expectedMessage = Json.writeValueAsString(aResult)

        every { updateMediaStatusUseCase.execute(any()) } just runs

        // when
        rabbitTemplate.convertAndSend(queueProperties.queue, expectedMessage)

        // then
        val invocationData = harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS)

        with(invocationData) {
            assertNotNull(this)
            assertNotNull(arguments)

            val actualMessage = arguments[0] as String

            assertEquals(expectedMessage, actualMessage)

            verify {
                updateMediaStatusUseCase.execute(
                    withArg {
                        assertEquals(expectedStatus, it.status)
                        assertEquals(expectedId, it.videoId)
                        assertEquals(expectedResourceId, it.resourceId)
                        assertEquals(expectedEncoderVideoFolder, it.folder)
                        assertEquals(expectedFilePath, it.filename)
                    }
                )
            }
        }
    }
}
