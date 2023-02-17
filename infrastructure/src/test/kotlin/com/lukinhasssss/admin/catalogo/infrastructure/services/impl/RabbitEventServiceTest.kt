package com.lukinhasssss.admin.catalogo.infrastructure.services.impl

import com.lukinhasssss.admin.catalogo.AmqpTest
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaCreated
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.json.Json
import com.lukinhasssss.admin.catalogo.infrastructure.services.EventService
import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@AmqpTest
class RabbitEventServiceTest {

    companion object {
        const val LISTENER = "video.created"
    }

    @Autowired
    @VideoCreatedQueue
    private lateinit var publisher: EventService

    @Autowired
    private lateinit var harness: RabbitListenerTestHarness

    @Test
    fun shouldSendMessage() {
        // given
        val notification = VideoMediaCreated("resource", "filepath")

        val expectedMessage = Json.writeValueAsString(notification)

        // when
        publisher.send(notification)

        // then
        val invocationData = harness.getNextInvocationDataFor(LISTENER, 1, SECONDS)

        with(invocationData) {
            assertNotNull(this)
            assertNotNull(arguments)

            val actualMessage = arguments[0] as String

            assertEquals(expectedMessage, actualMessage)
        }
    }

    @Component
    class VideoCreatedNewsListener {

        @RabbitListener(id = LISTENER, queues = ["\${amqp.queues.video-created.routing-key}"])
        fun onVideoCreated(@Payload message: String) = Logger.info(message = message)
    }
}
