package com.lukinhasssss.admin.catalogo.application.video.media.upload

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.video.MediaResourceGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.BANNER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL_HALF
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.TRAILER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.VIDEO
import com.lukinhasssss.admin.catalogo.domain.video.VideoResource
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UploadMediaUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultUploadMediaUseCase

    @MockK
    private lateinit var mediaResourceGateway: MediaResourceGateway

    @MockK
    private lateinit var videoGateway: VideoGateway

    @Test
    fun givenCommandToUpload_whenIsValid_shouldUpdateVideoMediaAndPersistIt() {
        // given
        val aVideo = Fixture.Videos.systemDesign()
        val expectedId = aVideo.id
        val expectedType = VIDEO
        val expectedResource = Fixture.Videos.resource(expectedType)
        val expectedVideoResource = VideoResource.with(expectedResource, expectedType)
        val expectedMedia = Fixture.Videos.audioVideo(expectedType)

        every { videoGateway.findById(any()) } returns aVideo
        every { mediaResourceGateway.storeAudioVideo(any(), any()) } returns expectedMedia
        every { videoGateway.update(any()) } answers { firstArg() }

        val aCommand = UploadMediaCommand.with(expectedId.value, expectedVideoResource)

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertEquals(expectedType, actualOutput.mediaType)
        assertEquals(expectedId.value, actualOutput.videoId)

        verify { videoGateway.findById(expectedId) }
        verify { mediaResourceGateway.storeAudioVideo(expectedId, expectedVideoResource) }

        verify {
            videoGateway.update(
                withArg {
                    assertEquals(expectedMedia, it.video)
                    assertNull(it.trailer)
                    assertNull(it.banner)
                    assertNull(it.thumbnail)
                    assertNull(it.thumbnailHalf)
                }
            )
        }
    }

    @Test
    fun givenCommandToUpload_whenIsValid_shouldUpdateTrailerMediaAndPersistIt() {
        // given
        val aVideo = Fixture.Videos.systemDesign()
        val expectedId = aVideo.id
        val expectedType = TRAILER
        val expectedResource = Fixture.Videos.resource(expectedType)
        val expectedVideoResource = VideoResource.with(expectedResource, expectedType)
        val expectedMedia = Fixture.Videos.audioVideo(expectedType)

        every { videoGateway.findById(any()) } returns aVideo
        every { mediaResourceGateway.storeAudioVideo(any(), any()) } returns expectedMedia
        every { videoGateway.update(any()) } answers { firstArg() }

        val aCommand = UploadMediaCommand.with(expectedId.value, expectedVideoResource)

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertEquals(expectedType, actualOutput.mediaType)
        assertEquals(expectedId.value, actualOutput.videoId)

        verify { videoGateway.findById(expectedId) }
        verify { mediaResourceGateway.storeAudioVideo(expectedId, expectedVideoResource) }

        verify {
            videoGateway.update(
                withArg {
                    assertNull(it.video)
                    assertEquals(expectedMedia, it.trailer)
                    assertNull(it.banner)
                    assertNull(it.thumbnail)
                    assertNull(it.thumbnailHalf)
                }
            )
        }
    }

    @Test
    fun givenCommandToUpload_whenIsValid_shouldUpdateBannerMediaAndPersistIt() {
        // given
        val aVideo = Fixture.Videos.systemDesign()
        val expectedId = aVideo.id
        val expectedType = BANNER
        val expectedResource = Fixture.Videos.resource(expectedType)
        val expectedVideoResource = VideoResource.with(expectedResource, expectedType)
        val expectedMedia = Fixture.Videos.imageMedia(expectedType)

        every { videoGateway.findById(any()) } returns aVideo
        every { mediaResourceGateway.storeImage(any(), any()) } returns expectedMedia
        every { videoGateway.update(any()) } answers { firstArg() }

        val aCommand = UploadMediaCommand.with(expectedId.value, expectedVideoResource)

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertEquals(expectedType, actualOutput.mediaType)
        assertEquals(expectedId.value, actualOutput.videoId)

        verify { videoGateway.findById(expectedId) }
        verify { mediaResourceGateway.storeImage(expectedId, expectedVideoResource) }

        verify {
            videoGateway.update(
                withArg {
                    assertNull(it.video)
                    assertNull(it.trailer)
                    assertEquals(expectedMedia, it.banner)
                    assertNull(it.thumbnail)
                    assertNull(it.thumbnailHalf)
                }
            )
        }
    }

    @Test
    fun givenCommandToUpload_whenIsValid_shouldUpdateThumbnailMediaAndPersistIt() {
        // given
        val aVideo = Fixture.Videos.systemDesign()
        val expectedId = aVideo.id
        val expectedType = THUMBNAIL
        val expectedResource = Fixture.Videos.resource(expectedType)
        val expectedVideoResource = VideoResource.with(expectedResource, expectedType)
        val expectedMedia = Fixture.Videos.imageMedia(expectedType)

        every { videoGateway.findById(any()) } returns aVideo
        every { mediaResourceGateway.storeImage(any(), any()) } returns expectedMedia
        every { videoGateway.update(any()) } answers { firstArg() }

        val aCommand = UploadMediaCommand.with(expectedId.value, expectedVideoResource)

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertEquals(expectedType, actualOutput.mediaType)
        assertEquals(expectedId.value, actualOutput.videoId)

        verify { videoGateway.findById(expectedId) }
        verify { mediaResourceGateway.storeImage(expectedId, expectedVideoResource) }

        verify {
            videoGateway.update(
                withArg {
                    assertNull(it.video)
                    assertNull(it.trailer)
                    assertNull(it.banner)
                    assertEquals(expectedMedia, it.thumbnail)
                    assertNull(it.thumbnailHalf)
                }
            )
        }
    }

    @Test
    fun givenCommandToUpload_whenIsValid_shouldUpdateThumbnailHalfMediaAndPersistIt() {
        // given
        val aVideo = Fixture.Videos.systemDesign()
        val expectedId = aVideo.id
        val expectedType = THUMBNAIL_HALF
        val expectedResource = Fixture.Videos.resource(expectedType)
        val expectedVideoResource = VideoResource.with(expectedResource, expectedType)
        val expectedMedia = Fixture.Videos.imageMedia(expectedType)

        every { videoGateway.findById(any()) } returns aVideo
        every { mediaResourceGateway.storeImage(any(), any()) } returns expectedMedia
        every { videoGateway.update(any()) } answers { firstArg() }

        val aCommand = UploadMediaCommand.with(expectedId.value, expectedVideoResource)

        // when
        val actualOutput = useCase.execute(aCommand)

        // then
        assertEquals(expectedType, actualOutput.mediaType)
        assertEquals(expectedId.value, actualOutput.videoId)

        verify { videoGateway.findById(expectedId) }
        verify { mediaResourceGateway.storeImage(expectedId, expectedVideoResource) }

        verify {
            videoGateway.update(
                withArg {
                    assertNull(it.video)
                    assertNull(it.trailer)
                    assertNull(it.banner)
                    assertNull(it.thumbnail)
                    assertEquals(expectedMedia, it.thumbnailHalf)
                }
            )
        }
    }

    @Test
    fun givenCommandToUpload_whenVideoIsInvalid_shouldReturnNotFound() {
        // given
        val aVideo = Fixture.Videos.systemDesign()
        val expectedId = aVideo.id
        val expectedType = THUMBNAIL_HALF
        val expectedResource = Fixture.Videos.resource(expectedType)
        val expectedVideoResource = VideoResource.with(expectedResource, expectedType)

        val expectedErrorMessage = "Video with ID ${expectedId.value} was not found"

        every { videoGateway.findById(any()) } returns null

        val aCommand = UploadMediaCommand.with(expectedId.value, expectedVideoResource)

        // when
        val actualException = assertThrows<NotFoundException> { useCase.execute(aCommand) }

        // then
        assertEquals(expectedErrorMessage, actualException.message)
    }
}
