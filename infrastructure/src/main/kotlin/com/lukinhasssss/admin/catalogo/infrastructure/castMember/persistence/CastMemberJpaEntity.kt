package com.lukinhasssss.admin.catalogo.infrastructure.castMember.persistence

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity(name = "CastMember")
@Table(name = "cast_members")
class CastMemberJpaEntity(

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
        fun from(aMember: CastMember): CastMemberJpaEntity = with(aMember) {
            CastMemberJpaEntity(
                id = id.value,
                name = name,
                type = type,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }

    fun toAggregate() = CastMember.with(
        anId = id, aName = name, aType = type, aCreationDate = createdAt, anUpdateDate = updatedAt
    )
}
