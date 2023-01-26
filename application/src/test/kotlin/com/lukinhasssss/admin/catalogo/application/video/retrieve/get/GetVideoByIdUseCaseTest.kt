package com.lukinhasssss.admin.catalogo.application.video.retrieve.get

import com.lukinhasssss.admin.catalogo.application.Fixture
import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.video.AudioVideoMedia
import com.lukinhasssss.admin.catalogo.domain.video.ImageMedia
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.PENDING
import com.lukinhasssss.admin.catalogo.domain.video.Resource
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Year
import java.util.UUID
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
        val expectedVideo = audioVideo(Resource.Type.VIDEO)
        val expectedTrailer = audioVideo(Resource.Type.TRAILER)
        val expectedBanner = image(Resource.Type.BANNER)
        val expectedThumb = image(Resource.Type.THUMBNAIL)
        val expectedThumbHalf = image(Resource.Type.THUMBNAIL_HALF)

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

        aVideo = aVideo.setVideo(expectedVideo)
        aVideo = aVideo.setTrailer(expectedTrailer)
        aVideo = aVideo.setBanner(expectedBanner)
        aVideo = aVideo.setThumbnail(expectedThumb)
        aVideo = aVideo.setThumbnailHalf(expectedThumbHalf)

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

    private fun audioVideo(type: Resource.Type): AudioVideoMedia {
        val checksum = UUID.randomUUID().toString()

        return AudioVideoMedia.with(
            checksum = checksum,
            name = type.name,
            rawLocation = "/videos/$checksum",
            encodedLocation = "",
            status = PENDING
        )
    }

    private fun image(type: Resource.Type): ImageMedia {
        val checksum = UUID.randomUUID().toString()

        return ImageMedia.with(
            checksum = checksum,
            name = type.name,
            location = "/images/$checksum"
        )
    }
}
