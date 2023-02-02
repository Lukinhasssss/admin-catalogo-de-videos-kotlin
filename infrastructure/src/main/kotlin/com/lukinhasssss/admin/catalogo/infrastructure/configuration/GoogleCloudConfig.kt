package com.lukinhasssss.admin.catalogo.infrastructure.configuration

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.GoogleCloudProperties
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.GoogleStorageProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile(value = ["development", "production"])
class GoogleCloudConfig {

    @Bean
    @ConfigurationProperties(value = "google.cloud")
    fun googleCloudProperties() = GoogleCloudProperties()

    @Bean
    @ConfigurationProperties(value = "google.cloud.storage.catalogo-video")
    fun googleStorageProperties() = GoogleStorageProperties()
}
