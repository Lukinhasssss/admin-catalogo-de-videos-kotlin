package com.lukinhasssss.admin.catalogo.infrastructure.configuration

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmqpConfig {

    @Bean
    @ConfigurationProperties(value = "amqp.queues.video-created")
    fun videoCreatedQueueProperties() = QueueProperties()

    @Bean
    @ConfigurationProperties(value = "amqp.queues.video-encoded")
    fun videoEncodedQueueProperties() = QueueProperties()
}
