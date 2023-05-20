package com.lukinhasssss.admin.catalogo.infrastructure.configuration

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties
import com.lukinhasssss.admin.catalogo.infrastructure.services.EventService
import com.lukinhasssss.admin.catalogo.infrastructure.services.impl.RabbitEventService
import com.lukinhasssss.admin.catalogo.infrastructure.services.local.InMemoryEventService
import org.springframework.amqp.rabbit.core.RabbitOperations
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class EventConfig {

    @Bean
    @VideoCreatedQueue
    @Profile(value = ["development"])
    fun inMemoryVideoCreatedEventService(): EventService = InMemoryEventService()

    @Bean
    @VideoCreatedQueue
    @ConditionalOnMissingBean
    fun videoCreatedEventService(
        @VideoCreatedQueue props: QueueProperties,
        ops: RabbitOperations
    ): EventService = RabbitEventService(
        exchange = props.exchange,
        routingKey = props.routingKey,
        ops = ops
    )
}
