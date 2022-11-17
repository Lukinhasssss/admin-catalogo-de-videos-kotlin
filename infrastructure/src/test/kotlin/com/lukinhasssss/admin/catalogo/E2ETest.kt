package com.lukinhasssss.admin.catalogo

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.WebServerConfig
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited

@ActiveProfiles("test-e2e")
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@SpringBootTest(
    classes = [WebServerConfig::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@ExtendWith(CleanUpExtension::class)
annotation class E2ETest