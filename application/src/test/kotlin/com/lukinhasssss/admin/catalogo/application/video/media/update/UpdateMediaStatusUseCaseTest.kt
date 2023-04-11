package com.lukinhasssss.admin.catalogo.application.video.media.update

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.COMPLETED
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.PROCESSING
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.TRAILER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.VIDEO
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UpdateMediaStatusUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultUpdateMediaStatusUseCase

    @MockK
    private lateinit var videoGateway: VideoGateway

    @Test
    fun givenACommandForVideo_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
        // given
        val expectedStatus = COMPLETED
        val expectedFolder = "encoded_media"
        val expectedFilename = "filename.mp4"
        val expectedType = VIDEO
        val expectedMedia = Fixture.Videos.audioVideo(expectedType)

        val aVideo = Fixture.Videos.systemDesign().updateVideoMedia(expectedMedia)

        val expectedId = aVideo.id

        every { videoGateway.findById(any()) } returns aVideo
        every { videoGateway.update(any()) } answers { firstArg() }

        val aCommand = UpdateMediaStatusCommand.with(
            videoId = expectedId.value,
            status = expectedStatus,
            resourceId = expectedMedia.id,
            folder = expectedFolder,
            filename = expectedFilename
        )

        // when
        useCase.execute(aCommand)

        // then
        verify { videoGateway.findById(expectedId) }

        verify {
            videoGateway.update(
                withArg {
                    assertNull(it.trailer)

                    val actualVideoMedia = it.video!!

                    with(actualVideoMedia) {
                        assertEquals(expectedMedia.id, id)
                        assertEquals(expectedMedia.rawLocation, rawLocation)
                        assertEquals(expectedMedia.checksum, checksum)
                        assertEquals(expectedStatus, status)
                        assertEquals("$expectedFolder/$expectedFilename", encodedLocation)
                    }
                }
            )
        }
    }

    @Test
    fun givenACommandForVideo_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        // given
        val expectedStatus = PROCESSING
        val expectedFolder = ""
        val expectedFilename = ""
        val expectedType = VIDEO
        val expectedMedia = Fixture.Videos.audioVideo(expectedType)

        val aVideo = Fixture.Videos.systemDesign().updateVideoMedia(expectedMedia)

        val expectedId = aVideo.id

        every { videoGateway.findById(any()) } returns aVideo
        every { videoGateway.update(any()) } answers { firstArg() }

        val aCommand = UpdateMediaStatusCommand.with(
            videoId = expectedId.value,
            status = expectedStatus,
            resourceId = expectedMedia.id,
            folder = expectedFolder,
            filename = expectedFilename
        )

        // when
        useCase.execute(aCommand)

        // then
        verify { videoGateway.findById(expectedId) }

        verify {
            videoGateway.update(
                withArg {
                    assertNull(it.trailer)

                    val actualVideoMedia = it.video!!

                    with(actualVideoMedia) {
                        assertEquals(expectedMedia.id, id)
                        assertEquals(expectedMedia.rawLocation, rawLocation)
                        assertEquals(expectedMedia.checksum, checksum)
                        assertEquals(expectedStatus, status)
                        assertTrue(encodedLocation.isBlank())
                    }
                }
            )
        }
    }

    @Test
    fun givenACommandForTrailer_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
        // given
        val expectedStatus = COMPLETED
        val expectedFolder = "encoded_media"
        val expectedFilename = "filename.mp4"
        val expectedType = TRAILER
        val expectedMedia = Fixture.Videos.audioVideo(expectedType)

        val aVideo = Fixture.Videos.systemDesign().updateTrailerMedia(expectedMedia)

        val expectedId = aVideo.id

        every { videoGateway.findById(any()) } returns aVideo
        every { videoGateway.update(any()) } answers { firstArg() }

        val aCommand = UpdateMediaStatusCommand.with(
            videoId = expectedId.value,
            status = expectedStatus,
            resourceId = expectedMedia.id,
            folder = expectedFolder,
            filename = expectedFilename
        )

        // when
        useCase.execute(aCommand)

        // then
        verify { videoGateway.findById(expectedId) }

        verify {
            videoGateway.update(
                withArg {
                    assertNull(it.video)

                    val actualVideoMedia = it.trailer!!

                    with(actualVideoMedia) {
                        assertEquals(expectedMedia.id, id)
                        assertEquals(expectedMedia.rawLocation, rawLocation)
                        assertEquals(expectedMedia.checksum, checksum)
                        assertEquals(expectedStatus, status)
                        assertEquals("$expectedFolder/$expectedFilename", encodedLocation)
                    }
                }
            )
        }
    }

    @Test
    fun givenACommandForTrailer_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        // given
        val expectedStatus = PROCESSING
        val expectedFolder = ""
        val expectedFilename = ""
        val expectedType = TRAILER
        val expectedMedia = Fixture.Videos.audioVideo(expectedType)

        val aVideo = Fixture.Videos.systemDesign().updateTrailerMedia(expectedMedia)

        val expectedId = aVideo.id

        every { videoGateway.findById(any()) } returns aVideo
        every { videoGateway.update(any()) } answers { firstArg() }

        val aCommand = UpdateMediaStatusCommand.with(
            videoId = expectedId.value,
            status = expectedStatus,
            resourceId = expectedMedia.id,
            folder = expectedFolder,
            filename = expectedFilename
        )

        // when
        useCase.execute(aCommand)

        // then
        verify { videoGateway.findById(expectedId) }

        verify {
            videoGateway.update(
                withArg {
                    assertNull(it.video)

                    val actualVideoMedia = it.trailer!!

                    with(actualVideoMedia) {
                        assertEquals(expectedMedia.id, id)
                        assertEquals(expectedMedia.rawLocation, rawLocation)
                        assertEquals(expectedMedia.checksum, checksum)
                        assertEquals(expectedStatus, status)
                        assertTrue(encodedLocation.isBlank())
                    }
                }
            )
        }
    }

    @Test
    fun givenACommandForTrailer_whenIsInvalid_shouldDoNothing() {
        // given
        val expectedStatus = COMPLETED
        val expectedFolder = "encoded_media"
        val expectedFilename = "filename.mp4"
        val expectedType = TRAILER
        val expectedMedia = Fixture.Videos.audioVideo(expectedType)

        val aVideo = Fixture.Videos.systemDesign().updateTrailerMedia(expectedMedia)

        val expectedId = aVideo.id

        every { videoGateway.findById(any()) } returns aVideo

        val aCommand = UpdateMediaStatusCommand.with(
            videoId = expectedId.value,
            status = expectedStatus,
            resourceId = "randomId",
            folder = expectedFolder,
            filename = expectedFilename
        )

        // when
        useCase.execute(aCommand)

        // then
        verify(exactly = 0) { videoGateway.update(any()) }
    }
}
