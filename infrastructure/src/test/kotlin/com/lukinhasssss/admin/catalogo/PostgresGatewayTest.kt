package com.lukinhasssss.admin.catalogo

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited

@ActiveProfiles("test")
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@DataJpaTest
@ComponentScan(
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = [".*[PostgresGateway]"])
    ]
)
@ExtendWith(CleanUpExtension::class)
annotation class PostgresGatewayTest
