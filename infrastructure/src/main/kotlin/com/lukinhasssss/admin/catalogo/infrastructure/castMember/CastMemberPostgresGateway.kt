package com.lukinhasssss.admin.catalogo.infrastructure.castMember

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence.CastMemberRepository
import org.springframework.stereotype.Component

@Component
class CastMemberPostgresGateway(
    private val castMemberRepository: CastMemberRepository
) : CastMemberGateway {

    override fun create(aCastMember: CastMember): CastMember {
        TODO("Not yet implemented")
    }

    override fun findById(anID: CastMemberID): CastMember? {
        TODO("Not yet implemented")
    }

    override fun findAll(aQuery: SearchQuery): Pagination<CastMember> {
        TODO("Not yet implemented")
    }

    override fun update(aCastMember: CastMember): CastMember {
        TODO("Not yet implemented")
    }

    override fun deleteById(anID: CastMemberID) {
        TODO("Not yet implemented")
    }
}
