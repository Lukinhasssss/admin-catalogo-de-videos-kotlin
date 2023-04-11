package com.lukinhasssss.admin.catalogo.domain

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberType.ACTOR
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.resource.Resource
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils
import com.lukinhasssss.admin.catalogo.domain.video.AudioVideoMedia
import com.lukinhasssss.admin.catalogo.domain.video.ImageMedia
import com.lukinhasssss.admin.catalogo.domain.video.Rating
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.TRAILER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.VIDEO
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

        fun mediaType() = FAKER.random.nextEnum(VideoMediaType.values())

        fun audioVideo(type: VideoMediaType): AudioVideoMedia {
            val id = IdUtils.uuid()
            val name = "${type.name.lowercase()}_$id"
            return AudioVideoMedia.with(checksum = id, name = name, rawLocation = "/raw/$name")
        }

        fun imageMedia(type: VideoMediaType): ImageMedia {
            val id = IdUtils.uuid()
            val name = "${type.name.lowercase()}_$id"
            return ImageMedia.with(checksum = id, name = name, location = "/raw/$name")
        }

        fun resource(type: VideoMediaType): Resource {
            val contentType = when (type) {
                VIDEO, TRAILER -> "video/mp4"
                else -> "image/jpg"
            }

            val checksum = IdUtils.uuid()
            val content = "Conteudo".toByteArray()

            return Resource.with(checksum, content, contentType, type.name.lowercase())
        }
    }
}
