package com.lukinhasssss.admin.catalogo.infrastructure

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.WebServerConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class AdminDoCatalogoDeVideos

fun main(args: Array<String>) {
    SpringApplication.run(WebServerConfig::class.java, *args)
}
