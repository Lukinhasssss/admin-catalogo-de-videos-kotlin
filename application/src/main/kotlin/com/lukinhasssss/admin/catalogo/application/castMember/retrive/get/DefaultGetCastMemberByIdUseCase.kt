package com.lukinhasssss.admin.catalogo.application.castMember.retrive.get

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException

class DefaultGetCastMemberByIdUseCase(
    private val castMemberGateway: CastMemberGateway
) : GetCastMemberByIdUseCase() {

    override fun execute(anIn: String): CastMemberOutput {
        val anId = CastMemberID.from(anIn)

        return castMemberGateway.findById(anId)?.map() ?: throw notFound(anId)
    }

    private fun CastMember.map() =
        CastMemberOutput(
            id = id.value,
            name = name,
            type = type,
            createdAt = createdAt,
            updatedAt = updatedAt
        )

    private fun notFound(anCastMemberID: CastMemberID) =
        NotFoundException.with(
            id = anCastMemberID,
            anAggregate = CastMember::class
        )
}
