package com.lukinhasssss.admin.catalogo

import com.lukinhasssss.admin.catalogo.infrastructure.configuration.WebServerConfig
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited

@ActiveProfiles("test-integration")
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@SpringBootTest(classes = [WebServerConfig::class])
annotation class AmqpTest
