package com.lukinhasssss.admin.catalogo

import com.rabbitmq.client.Channel
import io.mockk.every
import io.mockk.spyk
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.Connection
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.test.RabbitListenerTest
import org.springframework.amqp.rabbit.test.TestRabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Creates proxy around each class annotated with @{@link org.springframework.amqp.rabbit.annotation.RabbitListener}
 * that can be used to verify incoming messages via {@link org.springframework.amqp.rabbit.test.RabbitListenerTestHarness}.
 */
@Configuration
@RabbitListenerTest(spy = false, capture = true)
class AmqpTestConfiguration {

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory) = TestRabbitTemplate(connectionFactory)

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val factory = spyk<ConnectionFactory>()
        val connection = spyk<Connection>()
        val channel = spyk<Channel>()

        every { factory.createConnection() } returns connection
        every { connection.createChannel(any()) } returns channel
        every { channel.isOpen } returns true

        return factory
    }

    @Bean
    fun rabbitListenerContainerFactory(
        connectionFactory: ConnectionFactory
    ): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()

        factory.setConnectionFactory(connectionFactory)

        return factory
    }
}
