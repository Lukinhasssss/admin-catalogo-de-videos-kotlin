package com.lukinhasssss.admin.catalogo.infrastructure.video

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.video.AudioVideoMedia
import com.lukinhasssss.admin.catalogo.domain.video.ImageMedia
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.infrastructure.video.persistence.VideoRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.Year
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@IntegrationTest
class DefaultVideoGatewayTest {

    @Autowired
    private lateinit var videoGateway: DefaultVideoGateway

    @Autowired
    private lateinit var castMemberGateway: CastMemberGateway

    @Autowired
    private lateinit var categoryGateway: CategoryGateway

    @Autowired
    private lateinit var genreGateway: GenreGateway

    @Autowired
    private lateinit var videoRepository: VideoRepository

    @Test
    fun testInjection() {
        assertNotNull(videoGateway)
        assertNotNull(castMemberGateway)
        assertNotNull(categoryGateway)
        assertNotNull(genreGateway)
        assertNotNull(videoRepository)
    }

    @Test
    @Transactional
    fun givenAValidVideo_whenCallsCreate_shouldPersistIt() {
        // given
        val zoro = castMemberGateway.create(Fixture.CastMembers.zoro())
        val animes = categoryGateway.create(Fixture.Categories.animes())
        val shonen = genreGateway.create(Fixture.Genres.shonen())

        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(animes.id)
        val expectedGenres = setOf(shonen.id)
        val expectedMembers = setOf(zoro.id)

        val expectedVideo = AudioVideoMedia.with(checksum = "123", name = "video", rawLocation = "/media/video")
        val expectedTrailer = AudioVideoMedia.with(checksum = "123", name = "trailer", rawLocation = "/media/trailer")
        val expectedBanner = ImageMedia.with(checksum = "123", name = "banner", location = "/media/banner")
        val expectedThumb = ImageMedia.with(checksum = "123", name = "thumb", location = "/media/thumb")
        val expectedThumbHalf = ImageMedia.with(checksum = "123", name = "thumbHalf", location = "/media/thumbHalf")

        val aVideo = Video.newVideo(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        ).copy(
            video = expectedVideo,
            trailer = expectedTrailer,
            banner = expectedBanner,
            thumbnail = expectedThumb,
            thumbnailHalf = expectedThumbHalf
        )

        // when
        val actualVideo = videoGateway.create(aVideo)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertEquals(expectedVideo.name, video?.name)
            assertEquals(expectedTrailer.name, trailer?.name)
            assertEquals(expectedBanner.name, banner?.name)
            assertEquals(expectedThumb.name, thumbnail?.name)
            assertEquals(expectedThumbHalf.name, thumbnailHalf?.name)
        }

        val persistedVideo = videoRepository.findById(actualVideo.id.value).get()

        with(persistedVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, Year.of(yearLaunched))
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, getCategoriesID())
            assertEquals(expectedGenres, getGenresID())
            assertEquals(expectedMembers, getCastMembersID())
            assertEquals(expectedVideo.name, video?.name)
            assertEquals(expectedTrailer.name, trailer?.name)
            assertEquals(expectedBanner.name, banner?.name)
            assertEquals(expectedThumb.name, thumbnail?.name)
            assertEquals(expectedThumbHalf.name, thumbnailHalf?.name)
        }
    }

    @Test
    @Transactional
    fun givenAValidVideoWithoutRelations_whenCallsCreate_shouldPersistIt() {
        // given
        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf<CategoryID>()
        val expectedGenres = setOf<GenreID>()
        val expectedMembers = setOf<CastMemberID>()

        val aVideo = Video.newVideo(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        )

        // when
        val actualVideo = videoGateway.create(aVideo)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertNull(video)
            assertNull(trailer)
            assertNull(banner)
            assertNull(thumbnail)
            assertNull(thumbnailHalf)
        }

        val persistedVideo = videoRepository.findById(actualVideo.id.value).get()

        with(persistedVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, Year.of(yearLaunched))
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, getCategoriesID())
            assertEquals(expectedGenres, getGenresID())
            assertEquals(expectedMembers, getCastMembersID())
            assertNull(video)
            assertNull(trailer)
            assertNull(banner)
            assertNull(thumbnail)
            assertNull(thumbnailHalf)
        }
    }

    @Test
    @Transactional
    fun givenAValidVideo_whenCallsUpdate_shouldPersistIt() {
        // given
        val aVideo = videoGateway.create(
            Video.newVideo(
                aTitle = Fixture.title(),
                aDescription = Fixture.Videos.description(),
                aLaunchYear = Year.of(Fixture.year()),
                aDuration = Fixture.duration(),
                wasOpened = Fixture.bool(),
                wasPublished = Fixture.bool(),
                aRating = Fixture.Videos.rating(),
                categories = emptySet(),
                genres = emptySet(),
                members = emptySet()
            )
        )

        val zoro = castMemberGateway.create(Fixture.CastMembers.zoro())
        val animes = categoryGateway.create(Fixture.Categories.animes())
        val shonen = genreGateway.create(Fixture.Genres.shonen())

        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(animes.id)
        val expectedGenres = setOf(shonen.id)
        val expectedMembers = setOf(zoro.id)

        val expectedVideo = AudioVideoMedia.with(checksum = "123", name = "video", rawLocation = "/media/video")
        val expectedTrailer = AudioVideoMedia.with(checksum = "123", name = "trailer", rawLocation = "/media/trailer")
        val expectedBanner = ImageMedia.with(checksum = "123", name = "banner", location = "/media/banner")
        val expectedThumb = ImageMedia.with(checksum = "123", name = "thumb", location = "/media/thumb")
        val expectedThumbHalf = ImageMedia.with(checksum = "123", name = "thumbHalf", location = "/media/thumbHalf")

        val aVideoUpdated = aVideo.update(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        ).copy(
            video = expectedVideo,
            trailer = expectedTrailer,
            banner = expectedBanner,
            thumbnail = expectedThumb,
            thumbnailHalf = expectedThumbHalf
        )

        // when
        val actualVideo = videoGateway.update(aVideoUpdated)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertEquals(expectedVideo.name, video?.name)
            assertEquals(expectedTrailer.name, trailer?.name)
            assertEquals(expectedBanner.name, banner?.name)
            assertEquals(expectedThumb.name, thumbnail?.name)
            assertEquals(expectedThumbHalf.name, thumbnailHalf?.name)
            assertNotNull(createdAt)
            assertTrue(aVideo.updatedAt.isBefore(updatedAt))
        }

        val persistedVideo = videoRepository.findById(actualVideo.id.value).get()

        with(persistedVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, Year.of(yearLaunched))
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, getCategoriesID())
            assertEquals(expectedGenres, getGenresID())
            assertEquals(expectedMembers, getCastMembersID())
            assertEquals(expectedVideo.name, video?.name)
            assertEquals(expectedTrailer.name, trailer?.name)
            assertEquals(expectedBanner.name, banner?.name)
            assertEquals(expectedThumb.name, thumbnail?.name)
            assertEquals(expectedThumbHalf.name, thumbnailHalf?.name)
            assertNotNull(createdAt)
            assertTrue(aVideo.updatedAt.isBefore(updatedAt))
        }
    }
}
