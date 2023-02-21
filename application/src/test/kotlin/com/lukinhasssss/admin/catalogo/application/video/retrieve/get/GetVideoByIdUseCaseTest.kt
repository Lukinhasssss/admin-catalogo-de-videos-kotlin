package com.lukinhasssss.admin.catalogo.application.video.retrieve.get

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.BANNER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL_HALF
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.TRAILER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.VIDEO
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Year
import kotlin.test.assertEquals

class GetVideoByIdUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var usecase: DefaultGetVideoByIdUseCase

    @MockK
    private lateinit var videoGateway: VideoGateway

    @Test
    fun givenAValidId_whenCallalsGetVideo_shouldReturnIt() {
        // given
        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(Fixture.Categories.animes().id)
        val expectedGenres = setOf(Fixture.Genres.shonen().id)
        val expectedMembers = setOf(Fixture.CastMembers.luffy().id, Fixture.CastMembers.zoro().id)
        val expectedVideo = Fixture.Videos.audioVideo(VIDEO)
        val expectedTrailer = Fixture.Videos.audioVideo(TRAILER)
        val expectedBanner = Fixture.Videos.imageMedia(BANNER)
        val expectedThumb = Fixture.Videos.imageMedia(THUMBNAIL)
        val expectedThumbHalf = Fixture.Videos.imageMedia(THUMBNAIL_HALF)

        var aVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories,
            expectedGenres,
            expectedMembers
        )

        aVideo = aVideo.updateVideoMedia(expectedVideo)
        aVideo = aVideo.updateTrailerMedia(expectedTrailer)
        aVideo = aVideo.updateBannerMedia(expectedBanner)
        aVideo = aVideo.updateThumbnailMedia(expectedThumb)
        aVideo = aVideo.updateThumbnailHalfMedia(expectedThumbHalf)

        val expectedId = aVideo.id

        every { videoGateway.findById(any()) } returns aVideo

        // when
        val actualVideo = usecase.execute(expectedId.value)

        // then
        with(actualVideo) {
            assertEquals(expectedId.value, id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear.value, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories.asString().toSet(), categories)
            assertEquals(expectedGenres.asString().toSet(), genres)
            assertEquals(expectedMembers.asString().toSet(), castMembers)
            assertEquals(expectedVideo, video)
            assertEquals(expectedTrailer, trailer)
            assertEquals(expectedBanner, banner)
            assertEquals(expectedThumb, thumbnail)
            assertEquals(expectedThumbHalf, thumbnailHalf)
            assertEquals(aVideo.createdAt, createdAt)
            assertEquals(aVideo.updatedAt, updatedAt)
        }
    }

    @Test
    fun givenAnInvalidId_whenCallsGetVideo_shouldReturnNotFound() {
        // given
        val expectedId = VideoID.from("123")

        val expectedErrorMessage = "Video with ID ${expectedId.value} was not found"

        every { videoGateway.findById(any()) } returns null

        // when
        val actualError = assertThrows<NotFoundException> { usecase.execute(expectedId.value) }

        // then
        assertEquals(expectedErrorMessage, actualError.message)
    }
}
