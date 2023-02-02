package com.lukinhasssss.admin.catalogo.infrastructure.configuration

import com.google.api.gax.retrying.RetrySettings
import com.google.auth.Credentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.http.HttpTransportOptions
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.GoogleCloudProperties
import com.lukinhasssss.admin.catalogo.infrastructure.configuration.properties.GoogleStorageProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.threeten.bp.Duration
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.Base64

@Configuration
@Profile(value = ["development", "production"])
class GoogleCloudConfig {

    @Bean
    @ConfigurationProperties(value = "google.cloud")
    fun googleCloudProperties() = GoogleCloudProperties()

    @Bean
    @ConfigurationProperties(value = "google.cloud.storage.catalogo-video")
    fun googleStorageProperties() = GoogleStorageProperties()

    @Bean
    fun credentials(props: GoogleCloudProperties): Credentials {
        val jsonContent = Base64.getDecoder().decode(props.credentials)

        return try {
            val stream = ByteArrayInputStream(jsonContent)
            GoogleCredentials.fromStream(stream)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Bean
    fun storage(
        credentials: Credentials,
        storageProperties: GoogleStorageProperties
    ): Storage = with(storageProperties) {
        val transportOptions = HttpTransportOptions.newBuilder()
            .setConnectTimeout(connectTimeout)
            .setReadTimeout(readTimeout)
            .build()

        val retrySettings = RetrySettings.newBuilder()
            .setInitialRetryDelay(Duration.ofMillis(retryDelay))
            .setMaxRetryDelay(Duration.ofMillis(retryMaxDelay))
            .setMaxAttempts(retryMaxAttempts)
            .setRetryDelayMultiplier(retryMultiplier)
            .build()

        val options = StorageOptions.newBuilder()
            .setCredentials(credentials)
            .setTransportOptions(transportOptions)
            .setRetrySettings(retrySettings)
            .build()

        options.service
    }
}
