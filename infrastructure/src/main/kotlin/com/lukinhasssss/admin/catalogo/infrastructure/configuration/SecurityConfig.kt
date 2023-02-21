package com.lukinhasssss.admin.catalogo.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
class SecurityConfig {

    companion object {
        private const val ROLE_ADMIN = "CATALOGO_ADMIN"
        private const val ROLE_CAST_MEMBERS = "CATALOGO_CAST_MEMBERS"
        private const val ROLE_CATEGORIES = "CATALOGO_CATEGORIES"
        private const val ROLE_GENRES = "CATALOGO_GENRES"
        private const val ROLE_VIDEOS = "CATALOGO_VIDEOS"
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/cast_members*").hasAnyRole(ROLE_ADMIN, ROLE_CAST_MEMBERS)
                it.requestMatchers("/categories*").hasAnyRole(ROLE_ADMIN, ROLE_CATEGORIES)
                it.requestMatchers("/genres*").hasAnyRole(ROLE_ADMIN, ROLE_GENRES)
                it.requestMatchers("/videos*").hasAnyRole(ROLE_ADMIN, ROLE_VIDEOS)
                it.anyRequest().hasRole("ROLE_ADMIN")
            }
            .oauth2ResourceServer { it.jwt() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .headers { it.frameOptions().sameOrigin() }
            .build()
}
