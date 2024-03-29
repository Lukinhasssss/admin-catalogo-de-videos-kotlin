package com.lukinhasssss.admin.catalogo.infrastructure.configuration

import com.google.cloud.storage.Storage
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.google.GoogleStorageProperties
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.storage.StorageProperties
import com.lukinhasssss.admin.catalogo.infrastructure.services.StorageService
import com.lukinhasssss.admin.catalogo.infrastructure.services.impl.GoogleCloudStorageService
import com.lukinhasssss.admin.catalogo.infrastructure.services.local.InMemoryStorageService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class StorageConfig {

    @Bean
    @ConfigurationProperties(value = "storage.catalogo-videos")
    fun storageProperties() = StorageProperties()

    @Bean
    @Profile(value = ["development", "test-integration", "test-e2e"])
    fun inMemoryStorageService(): StorageService = InMemoryStorageService()

    @Bean
    @ConditionalOnMissingBean
    fun googleCloudStorageService(
        props: GoogleStorageProperties,
        storage: Storage
    ): StorageService = with(props) { GoogleCloudStorageService(bucket, storage) }
}
