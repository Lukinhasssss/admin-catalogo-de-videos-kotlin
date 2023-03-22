package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.UnitTest
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.utils.InstantUtils
import com.lukinhasssss.admin.catalogo.domain.validation.handler.ThrowsValidationHandler
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.PENDING
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Year
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class VideoTest : UnitTest() {

    @Test
    fun givenValidParams_whenCallNewVideo_shouldInstantiate() {
        // given
        val expectedTitle = "Video Title"
        val expectedDescription = "Video Description"
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())

        // when
        val actualVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories,
            expectedGenres,
            expectedMembers
        )

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertEquals(createdAt, updatedAt)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchedAt, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertNull(banner)
            assertNull(thumbnail)
            assertNull(thumbnailHalf)
            assertNull(trailer)
            assertNull(video)
            assertTrue(domainEvents.isEmpty())
        }

        assertDoesNotThrow { actualVideo.validate(ThrowsValidationHandler()) }
    }

    @Test
    fun givenValidVideo_whenCallsUpdates_shouldReturnUpdated() {
        // given
        val expectedTitle = "Video Title"
        val expectedDescription = "Video Description"
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())
        val expectedEvent = VideoMediaCreated("ID", "file")
        val expectedEventsCount = 1

        val aVideo = Video.newVideo(
            aTitle = "Any Title",
            aDescription = "Any Description",
            aLaunchYear = Year.of(1980),
            aDuration = 0.0,
            wasOpened = true,
            wasPublished = true,
            aRating = Rating.AGE_10,
            categories = emptySet(),
            genres = emptySet(),
            members = emptySet()
        )

        aVideo.registerEvent(expectedEvent)

        // when
        val actualVideo = aVideo.update(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchedAt,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        )

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(aVideo.createdAt, createdAt)
            assertTrue(aVideo.updatedAt.isBefore(updatedAt))
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchedAt, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertNull(banner)
            assertNull(thumbnail)
            assertNull(thumbnailHalf)
            assertNull(trailer)
            assertNull(video)
            assertEquals(expectedEventsCount, domainEvents.size)
            assertEquals(expectedEvent, domainEvents[0])
        }

        assertDoesNotThrow { actualVideo.validate(ThrowsValidationHandler()) }
    }

    @Test
    fun givenValidVideo_whenCallsSetVideo_shouldReturnUpdated() {
        // given
        val expectedTitle = "Video Title"
        val expectedDescription = "Video Description"
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())

        val aVideo = Video.newVideo(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchedAt,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        )

        val aVideoMedia = AudioVideoMedia.with(
            checksum = "abc",
            name = "Video.mp4",
            rawLocation = "/123/videos",
            encodedLocation = "",
            status = PENDING
        )

        // when
        val actualVideo = aVideo.updateVideoMedia(aVideoMedia)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(aVideo.createdAt, createdAt)
            assertTrue(aVideo.updatedAt.isBefore(updatedAt))
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchedAt, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertEquals(aVideoMedia, video)
            assertNull(banner)
            assertNull(thumbnail)
            assertNull(thumbnailHalf)
            assertNull(trailer)
        }

        assertDoesNotThrow { actualVideo.validate(ThrowsValidationHandler()) }
    }

    @Test
    fun givenValidVideo_whenCallsSetTrailer_shouldReturnUpdated() {
        // given
        val expectedTitle = "Video Title"
        val expectedDescription = "Video Description"
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())

        val aVideo = Video.newVideo(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchedAt,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        )

        val aTrailerMedia = AudioVideoMedia.with(
            checksum = "abc",
            name = "Trailer.mp4",
            rawLocation = "/123/videos",
            encodedLocation = "",
            status = PENDING
        )

        // when
        val actualVideo = aVideo.updateTrailerMedia(aTrailerMedia)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(aVideo.createdAt, createdAt)
            assertTrue(aVideo.updatedAt.isBefore(updatedAt))
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchedAt, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertEquals(aTrailerMedia, trailer)
            assertNull(video)
            assertNull(banner)
            assertNull(thumbnail)
            assertNull(thumbnailHalf)
        }

        assertDoesNotThrow { actualVideo.validate(ThrowsValidationHandler()) }
    }

    @Test
    fun givenValidVideo_whenCallsSetBanner_shouldReturnUpdated() {
        // given
        val expectedTitle = "Video Title"
        val expectedDescription = "Video Description"
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())

        val aVideo = Video.newVideo(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchedAt,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        )

        val aBannerMedia = ImageMedia.with(
            checksum = "abc",
            name = "Banner.mp4",
            location = "/123/videos"
        )

        // when
        val actualVideo = aVideo.updateBannerMedia(aBannerMedia)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(aVideo.createdAt, createdAt)
            assertTrue(aVideo.updatedAt.isBefore(updatedAt))
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchedAt, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertEquals(aBannerMedia, banner)
            assertNull(thumbnail)
            assertNull(thumbnailHalf)
            assertNull(video)
            assertNull(trailer)
        }

        assertDoesNotThrow { actualVideo.validate(ThrowsValidationHandler()) }
    }

    @Test
    fun givenValidVideo_whenCallsSetThumbnail_shouldReturnUpdated() {
        // given
        val expectedTitle = "Video Title"
        val expectedDescription = "Video Description"
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())

        val aVideo = Video.newVideo(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchedAt,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        )

        val aThumbMedia = ImageMedia.with(
            checksum = "abc",
            name = "Thumb.mp4",
            location = "/123/videos"
        )

        // when
        val actualVideo = aVideo.updateThumbnailMedia(aThumbMedia)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(aVideo.createdAt, createdAt)
            assertTrue(aVideo.updatedAt.isBefore(updatedAt))
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchedAt, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertEquals(aThumbMedia, thumbnail)
            assertNull(banner)
            assertNull(thumbnailHalf)
            assertNull(video)
            assertNull(trailer)
        }

        assertDoesNotThrow { actualVideo.validate(ThrowsValidationHandler()) }
    }

    @Test
    fun givenValidVideo_whenCallsSetThumbnailHalf_shouldReturnUpdated() {
        // given
        val expectedTitle = "Video Title"
        val expectedDescription = "Video Description"
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())

        val aVideo = Video.newVideo(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchedAt,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        )

        val aThumbMedia = ImageMedia.with(
            checksum = "abc",
            name = "ThumbHalf.mp4",
            location = "/123/videos"
        )

        // when
        val actualVideo = aVideo.updateThumbnailHalfMedia(aThumbMedia)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(aVideo.createdAt, createdAt)
            assertTrue(aVideo.updatedAt.isBefore(updatedAt))
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchedAt, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertEquals(aThumbMedia, thumbnailHalf)
            assertNull(banner)
            assertNull(thumbnail)
            assertNull(video)
            assertNull(trailer)
        }

        assertDoesNotThrow { actualVideo.validate(ThrowsValidationHandler()) }
    }

    @Test
    fun givenValidVideo_whenCallsWith_shouldCreateWithoutEvents() {
        // given
        val expectedTitle = "Video Title"
        val expectedDescription = "Video Description"
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())

        // when
        val aVideo = Video.with(
            anId = VideoID.unique(),
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchedAt,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers,
            aCreationDate = InstantUtils.now(),
            anUpdateDate = InstantUtils.now()
        )

        // then
        assertTrue(aVideo.domainEvents.isEmpty())
    }
}
