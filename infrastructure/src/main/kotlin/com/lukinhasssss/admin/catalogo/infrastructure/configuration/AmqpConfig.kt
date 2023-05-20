package com.lukinhasssss.admin.catalogo.infrastructure.configuration

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.annotations.VideoEvents
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@EnableRabbit
@Profile(value = ["!development"])
class AmqpConfig {

    @Bean
    @VideoCreatedQueue
    @ConfigurationProperties(value = "amqp.queues.video-created")
    fun videoCreatedQueueProperties(): QueueProperties = QueueProperties()

    @Bean
    @VideoEncodedQueue
    @ConfigurationProperties(value = "amqp.queues.video-encoded")
    fun videoEncodedQueueProperties(): QueueProperties = QueueProperties()

    @Bean
    fun queueName(@VideoCreatedQueue props: QueueProperties) = props.queue

    @Configuration
    @Profile(value = ["!development"])
    class Admin {

        @Bean
        @VideoEvents
        fun videoEventsExchange(
            @VideoCreatedQueue props: QueueProperties
        ): Exchange = DirectExchange(props.exchange)

        @Bean
        @VideoCreatedQueue
        fun videoCreatedQueue(@VideoCreatedQueue props: QueueProperties) = Queue(props.queue)

        @Bean
        @VideoCreatedQueue
        fun videoCreatedBinding(
            @VideoEvents exchange: DirectExchange,
            @VideoCreatedQueue queue: Queue,
            @VideoCreatedQueue props: QueueProperties
        ): Binding = BindingBuilder.bind(queue).to(exchange).with(props.routingKey)

        @Bean
        @VideoEncodedQueue
        fun videoEncodedQueue(@VideoEncodedQueue props: QueueProperties) = Queue(props.queue)

        @Bean
        @VideoEncodedQueue
        fun videoEncodedBinding(
            @VideoEvents exchange: DirectExchange,
            @VideoEncodedQueue queue: Queue,
            @VideoEncodedQueue props: QueueProperties
        ): Binding = BindingBuilder.bind(queue).to(exchange).with(props.routingKey)
    }
}
