package com.lukinhasssss.admin.catalogo.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import java.util.Optional
import java.util.stream.Collectors
import java.util.stream.Stream

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

        private const val REALM_ACCESS = "realm_access"
        private const val RESOURCE_ACCESS = "resource_access"
        private const val ROLES = "roles"

        private const val ROLE_PREFIX = "ROLE_"
        private const val SEPARATOR = "_"
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
            .oauth2ResourceServer {
                it.jwt().jwtAuthenticationConverter(KeycloakJwtConverter())
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .headers { it.frameOptions().sameOrigin() }
            .build()

    private class KeycloakJwtConverter(
        val authoritiesConverter: KeycloakAuthoritiesConverter = KeycloakAuthoritiesConverter()
    ) : Converter<Jwt, AbstractAuthenticationToken> {
        override fun convert(jwt: Jwt): AbstractAuthenticationToken =
            JwtAuthenticationToken(jwt, extractAuthorities(jwt), extractPrincipal(jwt))

        private fun extractPrincipal(jwt: Jwt): String? =
            jwt.getClaimAsString(JwtClaimNames.SUB)

        private fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority>? =
            authoritiesConverter.convert(jwt)
    }

    private class KeycloakAuthoritiesConverter : Converter<Jwt, Collection<GrantedAuthority>> {
        override fun convert(jwt: Jwt): Collection<GrantedAuthority>? {
            val realmRoles = extractRealmRoles(jwt)
            val resourceRoles = extractResourceRoles(jwt)

            return Stream.concat(realmRoles, resourceRoles)
                .map { SimpleGrantedAuthority(ROLE_PREFIX + it.uppercase()) }
                .collect(Collectors.toSet())
        }

        private fun extractRealmRoles(jwt: Jwt): Stream<String> =
            Optional.ofNullable(jwt.getClaimAsMap(REALM_ACCESS))
                .map { it[ROLES] as List<String> }
                .orElse(emptyList())
                .stream()

        private fun extractResourceRoles(jwt: Jwt): Stream<String> =
            Optional.ofNullable(jwt.getClaimAsMap(RESOURCE_ACCESS))
                .map { it.entries }
                .map { it.mapResources() }
                .orElse(emptyList())
                .stream()

        private fun MutableSet<MutableMap.MutableEntry<String, Any>>.mapResources() =
            stream().flatMap { it.mapResource() }.toList()

        private fun MutableMap.MutableEntry<String, Any>.mapResource(): Stream<String> {
            val key = this.key
            val value = this.value as Map<String, Any>
            val roles = value[ROLES] as List<String>

            return roles.stream().map { role -> key.plus(SEPARATOR).plus(role) }
        }
    }
}
