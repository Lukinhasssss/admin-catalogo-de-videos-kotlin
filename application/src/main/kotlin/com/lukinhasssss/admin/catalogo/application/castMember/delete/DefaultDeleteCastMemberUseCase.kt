package com.lukinhasssss.admin.catalogo.application.castMember.delete

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID

class DefaultDeleteCastMemberUseCase(
    private val castMemberGateway: CastMemberGateway
) : DeleteCastMemberUseCase() {

    override fun execute(anIn: String) =
        castMemberGateway.deleteById(CastMemberID.from(anIn))
}
