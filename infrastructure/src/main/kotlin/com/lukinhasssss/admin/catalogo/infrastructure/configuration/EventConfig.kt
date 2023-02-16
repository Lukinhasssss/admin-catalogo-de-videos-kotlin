package com.lukinhasssss.admin.catalogo.infrastructure.configuration

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties
import com.lukinhasssss.admin.catalogo.infrastructure.services.impl.RabbitEventService
import org.springframework.amqp.rabbit.core.RabbitOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventConfig {

    @Bean
    @VideoCreatedQueue
    fun videoCreatedEventService(
        @VideoCreatedQueue props: QueueProperties,
        ops: RabbitOperations
    ) = RabbitEventService(
        exchange = props.exchange, routingKey = props.routingKey, ops = ops
    )
}
