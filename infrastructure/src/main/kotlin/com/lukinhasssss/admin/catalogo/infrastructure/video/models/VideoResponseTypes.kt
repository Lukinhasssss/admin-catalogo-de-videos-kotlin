package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.annotation.JsonSubTypes
import kotlin.annotation.AnnotationRetention.RUNTIME

@Target(AnnotationTarget.CLASS)
@Retention(value = RUNTIME)
@JacksonAnnotationsInside
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(value = VideoEncoderCompleted::class),
        JsonSubTypes.Type(value = VideoEncoderError::class)
    ]
)
annotation class VideoResponseTypes
