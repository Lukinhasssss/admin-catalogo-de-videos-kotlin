package com.lukinhasssss.admin.catalogo

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import io.github.serpro69.kfaker.Faker

class Fixture {

    companion object {
        private val FAKER = Faker()

        fun name() = FAKER.onePiece.characters()
    }

    object CastMember {
        fun type() = FAKER.random.nextEnum(CastMemberType::class.java)
    }
}
