package com.lukinhasssss.admin.catalogo.infrastructure.configuration

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmqpConfig {

    @Bean
    @VideoCreatedQueue
    @ConfigurationProperties(value = "amqp.queues.video-created")
    fun videoCreatedQueueProperties() = QueueProperties()

    @Bean
    @VideoEncodedQueue
    @ConfigurationProperties(value = "amqp.queues.video-encoded")
    fun videoEncodedQueueProperties() = QueueProperties()

    @Bean
    fun queueName(
        @VideoCreatedQueue props: QueueProperties
    ) = props.queue
}
