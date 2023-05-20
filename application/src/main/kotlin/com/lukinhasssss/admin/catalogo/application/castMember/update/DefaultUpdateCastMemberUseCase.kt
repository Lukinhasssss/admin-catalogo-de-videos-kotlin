package com.lukinhasssss.admin.catalogo.application.castMember.update

import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification

class DefaultUpdateCastMemberUseCase(
    private val castMemberGateway: CastMemberGateway
) : UpdateCastMemberUseCase() {

    override fun execute(anIn: UpdateCastMemberCommand): UpdateCastMemberOutput = with(anIn) {
        val anId = CastMemberID.from(anIn.id)

        val aMember = castMemberGateway.findById(anId) ?: throw notFound(anId)

        val notification = Notification.create()

        val anUpdatedCastMember = notification.validate { aMember.update(name, type) }

        if (notification.hasError()) {
            notify(notification)
        }

        return UpdateCastMemberOutput.from(castMemberGateway.update(anUpdatedCastMember!!))
    }

    private fun notify(notification: Notification) {
        throw NotificationException(
            message = "Could not update Aggregate CastMember",
            notification = notification
        )
    }

    private fun notFound(anId: Identifier) =
        NotFoundException.with(anId, CastMember::class)
}
