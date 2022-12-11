package com.lukinhasssss.admin.catalogo.application.castMember.retrive.list

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery

class DefaultListCastMemberUseCase(
    private val castMemberGateway: CastMemberGateway
) : ListCastMemberUseCase() {

    override fun execute(anIn: SearchQuery): Pagination<CastMemberListOutput> {
        return castMemberGateway.findAll(anIn).map { CastMemberListOutput.from(it) }
    }
}
