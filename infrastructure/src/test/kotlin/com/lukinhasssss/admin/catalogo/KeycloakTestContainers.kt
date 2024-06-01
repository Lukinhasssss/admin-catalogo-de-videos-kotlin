package com.lukinhasssss.admin.catalogo

import dasniko.testcontainers.keycloak.KeycloakContainer
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container

@E2ETest
interface KeycloakTestContainers {

    companion object {
        const val GRANT_TYPE = "password"
        const val USERNAME = "lukinhasssss"
        const val PASSWORD = "123456"
        const val CLIENT_ID = "admin-do-catalogo"

        // NOTE: Quando é feito o import do realm o secret fica dessa forma
        const val CLIENT_SECRET = "**********"

        @Container
        var keycloak: KeycloakContainer =
            KeycloakContainer("quay.io/keycloak/keycloak:24.0.2")
                .withRealmImportFile("/keycloak/realm-export.json")

        init {
            keycloak.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerResourceServerIssuerProperty(registry: DynamicPropertyRegistry) {
            registry.add("keycloak.host") {
                "http://localhost:${keycloak.getMappedPort(8080)}"
            }
            registry.add("keycloak.realm") { "codeflix" }
        }
    }

    fun getAccessToken(): String {
        val url = keycloak.authServerUrl.plus("/realms/codeflix/protocol/openid-connect/token")

        var token = ""

        Given {
            contentType(ContentType.URLENC)
            formParam("grant_type", GRANT_TYPE)
            formParam("client_id", CLIENT_ID)
            formParam("client_secret", CLIENT_SECRET)
            formParam("username", USERNAME)
            formParam("password", PASSWORD)
        } When {
            post(url)
        } Then {
            token = extract().jsonPath().getString("access_token")
        }

        return "Bearer $token"
    }
}
