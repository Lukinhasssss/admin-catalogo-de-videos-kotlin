package com.lukinhasssss.admin.catalogo

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.ObjectMapperConfig
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited

@ActiveProfiles("test")
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@JsonTest(
    includeFilters = [
        ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [ObjectMapperConfig::class])
    ]
)
annotation class JacksonTest
