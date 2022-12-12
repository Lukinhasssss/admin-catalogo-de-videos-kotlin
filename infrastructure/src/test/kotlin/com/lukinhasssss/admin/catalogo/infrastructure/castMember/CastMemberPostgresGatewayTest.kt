package com.lukinhasssss.admin.catalogo.infrastructure.castMember

import com.lukinhasssss.admin.catalogo.PostgresGatewayTest
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@PostgresGatewayTest
class CastMemberPostgresGatewayTest {

    @Autowired
    private lateinit var castMemberGateway: CastMemberGateway

    @Autowired
    private lateinit var castMemberRepository: CastMemberRepository

    @Test
    fun testDependencies() {
        assertNotNull(castMemberGateway)
        assertNotNull(castMemberRepository)
    }
}
