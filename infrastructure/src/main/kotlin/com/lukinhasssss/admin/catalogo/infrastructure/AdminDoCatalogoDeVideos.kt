package com.lukinhasssss.admin.catalogo.infrastructure

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.WebServerConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.env.AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME

@SpringBootApplication
class AdminDoCatalogoDeVideos

fun main(args: Array<String>) {
    System.setProperty(DEFAULT_PROFILES_PROPERTY_NAME, "development")
    SpringApplication.run(WebServerConfig::class.java, *args)
}
