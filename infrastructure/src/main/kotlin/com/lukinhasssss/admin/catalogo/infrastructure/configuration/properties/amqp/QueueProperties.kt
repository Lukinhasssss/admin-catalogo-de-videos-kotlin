package com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.amqp

import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
import org.springframework.beans.factory.InitializingBean

data class QueueProperties(
    var exchange: String = String(),
    var routingKey: String = String(),
    var queue: String = String()
) : InitializingBean {

    override fun afterPropertiesSet() {
        Logger.debug(message = toString())
    }
}
