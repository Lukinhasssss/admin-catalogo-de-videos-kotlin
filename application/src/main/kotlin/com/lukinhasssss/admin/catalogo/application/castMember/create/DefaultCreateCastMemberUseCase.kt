package com.lukinhasssss.admin.catalogo.application.castMember.create

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification

class DefaultCreateCastMemberUseCase(
    private val castMemberGateway: CastMemberGateway
) : CreateCastMemberUseCase() {

    override fun execute(anIn: CreateCastMemberCommand): CreateCastMemberOutput {
        val (aName, aType) = anIn

        val notification = Notification.create()

        val aMember = notification.validate { CastMember.newMember(aName, aType) }

        if (notification.hasError()) notify(notification)

        return CreateCastMemberOutput.from(castMemberGateway.create(aMember!!))
    }

    private fun notify(notification: Notification) {
        throw NotificationException(
            message = "Could not create Aggregate CastMember",
            notification = notification
        )
    }
}
