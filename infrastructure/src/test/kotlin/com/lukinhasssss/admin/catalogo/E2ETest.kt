package com.lukinhasssss.admin.catalogo

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.WebServerConfig
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited

@ActiveProfiles("test-e2e")
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@DirtiesContext
@SpringBootTest(
    classes = [WebServerConfig::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@ExtendWith(PostgresCleanUpExtension::class)
@Tag(value = "e2eTest")
annotation class E2ETest

/**
 * Test annotation which indicates that the ApplicationContext associated with a test is dirty
 * and should therefore be closed and removed from the context cache.
 **/
