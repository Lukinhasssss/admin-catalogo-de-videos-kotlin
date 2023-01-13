package com.lukinhasssss.admin.catalogo.infrastructure.utils.log

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory

object Logger {

    fun info(
        logCode: String = "",
        message: String,
        payload: Any? = null
    ) {
        val callerName = Thread.currentThread().stackTrace[3].className

        val logger = LoggerFactory.getLogger(callerName)

        val finalMessage = getFinalMessage(logCode, message, payload)

        logger.info(finalMessage)
    }

    fun warning(
        logCode: String = "",
        message: String,
        payload: Any? = null
    ) {
        val callerName = Thread.currentThread().stackTrace[3].className

        val logger = LoggerFactory.getLogger(callerName)

        val finalMessage = getFinalMessage(logCode, message, payload)

        logger.warn(finalMessage)
    }

    fun error(
        logCode: String = "",
        message: String,
        payload: Any? = null
    ) {
        val callerName = Thread.currentThread().stackTrace[3].className

        val logger = LoggerFactory.getLogger(callerName)

        val finalMessage = getFinalMessage(logCode, message, payload)

        logger.error(finalMessage)
    }

    private fun getFinalMessage(logCode: String, message: String, payload: Any?): String {
        val finalMessage = mapOf(
            "log_code" to logCode,
            "log_message" to message,
            "payload" to payload
        )

        return ObjectMapper().writeValueAsString(finalMessage)
    }
}
