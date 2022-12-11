package com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "CastMember")
@Table(name = "cast_members")
data class CastMemberJpaEntity(

    @Id
    val id: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Enumerated(value = STRING)
    @Column(name = "type", nullable = false)
    val type: CastMemberType,

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    val updatedAt: Instant
) {

    companion object {
        fun from(aMember: CastMember): CastMemberJpaEntity {
            val (anId, aName, aType, createdAt, updatedAt) = aMember

            return CastMemberJpaEntity(
                id = anId.value,
                name = aName,
                type = aType,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }

    fun toAggregate() = CastMember.with(
        anId = id, aName = name, aType = type, aCreationDate = createdAt, anUpdateDate = updatedAt
    )
}
