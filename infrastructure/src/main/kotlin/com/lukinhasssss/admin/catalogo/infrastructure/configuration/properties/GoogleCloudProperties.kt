package com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties

import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
import org.springframework.beans.factory.InitializingBean

data class GoogleCloudProperties(
    var projectId: String = String(),
    var credentials: String = String()
) : InitializingBean {

    override fun afterPropertiesSet() {
        Logger.warning(message = toString())
    }
}
