package com.lukinhasssss.admin.catalogo

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited

@ActiveProfiles("test-integration")
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@DataJpaTest
@ComponentScan(
    basePackages = ["com.lukinhasssss.admin.catalogo"],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = [".[PostgresGateway]"])
    ]
)
@ExtendWith(PostgresCleanUpExtension::class)
annotation class PostgresGatewayTest
