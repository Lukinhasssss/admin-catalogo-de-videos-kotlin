package com.lukinhasssss.admin.catalogo.infrastructure.configuration.annotations

import org.springframework.beans.factory.annotation.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.TYPE_PARAMETER
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

@Qualifier(value = "VideoCreatedQueue")
@Retention(value = RUNTIME)
@Target(FIELD, TYPE_PARAMETER, VALUE_PARAMETER, FUNCTION)
annotation class VideoCreatedQueue
