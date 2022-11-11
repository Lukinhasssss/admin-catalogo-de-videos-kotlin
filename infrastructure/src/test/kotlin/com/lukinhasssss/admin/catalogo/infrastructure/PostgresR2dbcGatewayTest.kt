package com.lukinhasssss.admin.catalogo.infrastructure

import com.lukinhasssss.admin.catalogo.infrastructure.PostgresR2dbcGatewayTest.CleanUpExtensions
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.lang.annotation.Inherited
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE

@ActiveProfiles("test")
@Target(TYPE, CLASS)
@Retention(RUNTIME)
@Inherited
@DataR2dbcTest
@ComponentScan
@ExtendWith(CleanUpExtensions::class)
annotation class PostgresR2dbcGatewayTest {
    class CleanUpExtensions : BeforeEachCallback {
        override fun beforeEach(context: ExtensionContext) {
            val repositories = SpringExtension.getApplicationContext(context)
                .getBeansOfType(ReactiveCrudRepository::class.java)
                .values

            cleanUp(repositories)
        }

        private fun cleanUp(repositories: MutableCollection<ReactiveCrudRepository<*, *>>) {
            repositories.forEach { it.deleteAll() }
        }
    }
}

/*
@ComponentScan(includeFilters = [
    ComponentScan.Filter(type = FilterType.REGEX, pattern = [".*PostgresR2dbcGateway"])
    // ComponentScan.Filter(type = FilterType.REGEX, pattern = [".*[PostgresR2dbcGateway]"])
])
*/
