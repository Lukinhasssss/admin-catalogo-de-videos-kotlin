package com.lukinhasssss.admin.catalogo.domain.castMember

import com.lukinhasssss.admin.catalogo.domain.AggregateRoot
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.utils.InstantUtils
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import java.time.Instant

data class CastMember(
    override val id: CastMemberID,
    val name: String,
    val type: CastMemberType,
    val createdAt: Instant,
    val updatedAt: Instant
) : AggregateRoot<CastMemberID>(id) {

    init {
        val notification = Notification.create()
        validate(notification)

        if (notification.hasError()) {
            throw NotificationException(
                notification = notification,
                message = "Failed to create Aggregate CastMember"
            )
        }
    }

    override fun validate(handler: ValidationHandler) =
        CastMemberValidator(this, handler).validate()

    companion object {
        fun newMember(aName: String, aType: CastMemberType): CastMember {
            val anId = CastMemberID.unique()
            val now = InstantUtils.now()

            return CastMember(id = anId, name = aName, type = aType, createdAt = now, updatedAt = now)
        }

        fun with(
            anId: String,
            aName: String,
            aType: CastMemberType,
            aCreationDate: Instant,
            anUpdateDate: Instant
        ) = CastMember(CastMemberID.from(anId), aName, aType, aCreationDate, anUpdateDate)

        fun with(aMember: CastMember) = with(aMember) {
            CastMember(id, name, type, createdAt, updatedAt)
        }
    }

    fun update(aName: String, type: CastMemberType) =
        CastMember(id = id, name = aName, type = type, createdAt = createdAt, updatedAt = InstantUtils.now())
}
