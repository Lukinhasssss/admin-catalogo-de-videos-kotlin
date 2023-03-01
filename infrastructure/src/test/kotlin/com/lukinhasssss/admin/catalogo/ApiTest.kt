package com.lukinhasssss.admin.catalogo

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt

interface ApiTest {

    companion object {
        val ADMIN_JWT: SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor =
            jwt().authorities(SimpleGrantedAuthority("ROLE_CATALOGO_ADMIN"))

        val CATEGORIES_JWT: SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor =
            jwt().authorities(SimpleGrantedAuthority("ROLE_CATALOGO_CATEGORIES"))

        val CAST_MEMBERS_JWT: SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor =
            jwt().authorities(SimpleGrantedAuthority("ROLE_CATALOGO_CAST_MEMBERS"))

        val GENRES_JWT: SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor =
            jwt().authorities(SimpleGrantedAuthority("ROLE_CATALOGO_GENRES"))

        val VIDEOS_JWT: SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor =
            jwt().authorities(SimpleGrantedAuthority("ROLE_CATALOGO_VIDEOS"))
    }
}
