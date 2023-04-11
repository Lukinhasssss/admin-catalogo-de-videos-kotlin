package com.lukinhasssss.admin.catalogo.infrastructure

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.WebServerConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.env.AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME

@SpringBootApplication
class AdminDoCatalogoDeVideos

fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger(AdminDoCatalogoDeVideos::class.java)

    System.setProperty(DEFAULT_PROFILES_PROPERTY_NAME, "development")
    SpringApplication.run(WebServerConfig::class.java, *args)

    logger.info("Microsserviço de Administração do Catálogo de Vídeos inicializado com sucesso")
}
