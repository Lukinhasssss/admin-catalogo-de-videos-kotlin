package com.lukinhasssss.admin.catalogo.domain.castMember

import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.pagination.SearchQuery

interface CastMemberGateway {
    fun create(aCastMember: CastMember): CastMember

    fun findById(anID: CastMemberID): CastMember?

    fun findAll(aQuery: SearchQuery): Pagination<CastMember>

    fun update(aCastMember: CastMember): CastMember

    fun deleteById(anID: CastMemberID)

    fun existsByIds(castMemberIDs: Iterable<CastMemberID>): List<CastMemberID>
}
