package com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties

import com.lukinhasssss.admin.catalogo.infrastructure.utils.log.Logger
import org.springframework.beans.factory.InitializingBean

data class GoogleStorageProperties(
    var bucket: String = String(),
    var connectTimeout: Int = Int.MIN_VALUE,
    var readTimeout: Int = Int.MIN_VALUE,
    var retryDelay: Long = Long.MIN_VALUE,
    var retryMaxDelay: Long = Long.MIN_VALUE,
    var retryMaxAttempts: Int = Int.MIN_VALUE,
    var retryMultiplier: Double = Double.MIN_VALUE
) : InitializingBean {

    override fun afterPropertiesSet() {
        Logger.info(message = "", payload = this)
    }
}
