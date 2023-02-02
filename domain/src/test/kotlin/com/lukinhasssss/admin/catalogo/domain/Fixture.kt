package com.lukinhasssss.admin.catalogo.domain

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType.ACTOR
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.video.Rating
import com.lukinhasssss.admin.catalogo.domain.video.Resource
import com.lukinhasssss.admin.catalogo.domain.video.Resource.Type
import com.lukinhasssss.admin.catalogo.domain.video.Video
import io.github.serpro69.kfaker.Faker
import java.time.Year

class Fixture {

    companion object {
        private val FAKER = Faker()

        fun name() = FAKER.onePiece.characters()
        fun year() = FAKER.random.nextInt(2010, 2030)
        fun title() = FAKER.movie.title()
        fun bool() = FAKER.random.nextBoolean()
        fun duration() = FAKER.random.randomValue(listOf(120.0, 15.5, 35.5, 10.0, 2.0))

        fun video() = Video.newVideo(
            aTitle = title(),
            aDescription = Videos.description(),
            aLaunchYear = Year.of(year()),
            aDuration = duration(),
            wasOpened = bool(),
            wasPublished = bool(),
            aRating = Videos.rating(),
            categories = setOf(Categories.animes().id),
            genres = setOf(Genres.shonen().id),
            members = setOf(CastMembers.zoro().id)
        )
    }

    object Categories {
        private val ANIMES = Category.newCategory("Animes", "Some description", true)
        private val FILMES = Category.newCategory("Filmes", "Some description", true)

        fun animes() = ANIMES
        fun filmes() = FILMES
    }

    object Genres {
        private val SHONEN = Genre.newGenre("Shonen", true)
        private val ACAO = Genre.newGenre("Ação", true)

        fun shonen() = SHONEN
        fun acao() = ACAO
    }

    object CastMembers {
        private val LUFFY = CastMember.newMember("Monkey D Luffy", ACTOR)
        private val ZORO = CastMember.newMember("Roronoa Zoro", ACTOR)
        private val THE_ROCK = CastMember.newMember("Dwayne Johnson", ACTOR)

        fun type() = FAKER.random.nextEnum(CastMemberType.values())
        fun luffy() = LUFFY
        fun zoro() = ZORO
        fun theRock() = THE_ROCK
    }

    object Videos {
        fun systemDesign() = Video.newVideo(
            aTitle = title(),
            aDescription = description(),
            aLaunchYear = Year.of(year()),
            aDuration = duration(),
            wasOpened = bool(),
            wasPublished = bool(),
            aRating = rating(),
            categories = setOf(Categories.animes().id),
            genres = setOf(Genres.shonen().id),
            members = setOf(CastMembers.zoro().id)
        )

        fun description() = FAKER.movie.quote()

        fun rating() = FAKER.random.nextEnum(Rating.values())

        fun resource(type: Type): Resource {
            val contentType = when (type) {
                Type.VIDEO, Type.TRAILER -> "video/mp4"
                else -> "image/jpg"
            }

            val content = "Conteudo".toByteArray()

            return Resource.with(content, contentType, type.name.lowercase(), type)
        }
    }
}
