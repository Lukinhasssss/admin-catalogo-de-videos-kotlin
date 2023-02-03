package com.lukinhasssss.admin.catalogo.infrastructure.configuration

import com.google.cloud.storage.Storage
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.GoogleStorageProperties
import com.lukinhasssss.admin.catalogo.infrastructure.services.impl.GoogleCloudStorageService
import com.lukinhasssss.admin.catalogo.infrastructure.services.local.InMemoryStorageService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class StorageConfig {

    @Bean(name = ["storageService"])
    @Profile(value = ["development", "production"])
    fun googleCloudStorageService(
        props: GoogleStorageProperties,
        storage: Storage
    ) = with(props) { GoogleCloudStorageService(bucket, storage) }

    @Bean(name = ["storageService"])
    @ConditionalOnMissingBean
    fun inMemoryStorageService() = InMemoryStorageService()
}
