package com.lukinhasssss.admin.catalogo.infrastructure.video

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.Fixture.Videos.mediaType
import com.lukinhasssss.admin.catalogo.domain.Fixture.Videos.resource
import com.lukinhasssss.admin.catalogo.domain.video.MediaResourceGateway
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.PENDING
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.BANNER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.TRAILER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.VIDEO
import com.lukinhasssss.admin.catalogo.domain.video.VideoResource
import com.lukinhasssss.admin.catalogo.infrastructure.services.StorageService
import com.lukinhasssss.admin.catalogo.infrastructure.services.local.InMemoryStorageService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertNull

@IntegrationTest
class DefaultMediaResourceGatewayTest {

    @Autowired
    private lateinit var storageService: StorageService

    @Autowired
    private lateinit var mediaResourceGateway: MediaResourceGateway

    @BeforeEach
    fun setUp() = storageService().clear()

    @Test
    fun testInjection() {
        assertNotNull(storageService)
        assertInstanceOf(InMemoryStorageService::class.java, storageService)
        assertNotNull(mediaResourceGateway)
        assertInstanceOf(DefaultMediaResourceGateway::class.java, mediaResourceGateway)
    }

    @Test
    fun givenAValidResource_whenCallsStorageAudioVideo_shouldStoreIt() {
        // given
        val expectedVideoId = VideoID.unique()
        val expectedType = VIDEO
        val expectedResource = resource(expectedType)
        val expectedLocation = getExpectedLocation(expectedVideoId, expectedType)
        val expectedStatus = PENDING
        val expectedEncodedLocation = ""

        // when
        val actualMedia = mediaResourceGateway.storeAudioVideo(
            anId = expectedVideoId,
            videoResource = VideoResource.with(expectedResource, expectedType)
        )

        // then
        with(actualMedia) {
            assertNotNull(id)
            assertEquals(expectedLocation, rawLocation)
            assertEquals(expectedResource.name, name)
            assertEquals(expectedResource.checksum, checksum)
            assertEquals(expectedStatus, status)
            assertEquals(expectedEncodedLocation, encodedLocation)
        }

        val actualStored = storageService().storage()[expectedLocation]

        assertEquals(expectedResource, actualStored)
    }

    @Test
    fun givenAValidResource_whenCallsStorageImage_shouldStoreIt() {
        // given
        val expectedVideoId = VideoID.unique()
        val expectedType = BANNER
        val expectedResource = resource(expectedType)
        val expectedLocation = getExpectedLocation(expectedVideoId, expectedType)

        // when
        val actualMedia = mediaResourceGateway.storeImage(
            anId = expectedVideoId,
            imageResource = VideoResource.with(expectedResource, expectedType)
        )

        // then
        with(actualMedia) {
            assertNotNull(id)
            assertEquals(expectedLocation, location)
            assertEquals(expectedResource.name, name)
            assertEquals(expectedResource.checksum, checksum)
        }

        val actualStored = storageService().storage()[expectedLocation]

        assertEquals(expectedResource, actualStored)
    }

    @Test
    fun givenAValidVideoId_whenCallsClearResources_shouldDeleteAll() {
        // given
        val videoOne = VideoID.unique()
        val videoTwo = VideoID.unique()

        val toBeDeleted = mutableListOf(
            getExpectedLocation(videoOne, VIDEO),
            getExpectedLocation(videoOne, TRAILER),
            getExpectedLocation(videoOne, BANNER)
        )

        val expectedValues = mutableListOf(
            getExpectedLocation(videoTwo, VIDEO),
            getExpectedLocation(videoTwo, BANNER)
        )

        toBeDeleted.forEach { storageService().store(it, resource(mediaType())) }
        expectedValues.forEach { storageService().store(it, resource(mediaType())) }

        assertEquals(5, storageService().storage().size)

        // when
        mediaResourceGateway.clearResources(videoOne)

        // then
        assertEquals(2, storageService().storage().size)

        val actualKeys = storageService().storage().keys

        assertTrue(expectedValues.size == actualKeys.size && actualKeys.containsAll(expectedValues))
    }

    @Test
    fun givenAnInvalidType_whenCallsGetResources_shouldReturnNull() {
        // given
        val videoId = VideoID.unique()
        val expectedType = THUMBNAIL

        storageService().store(getExpectedLocation(videoId, VIDEO), resource(mediaType()))
        storageService().store(getExpectedLocation(videoId, TRAILER), resource(mediaType()))
        storageService().store(getExpectedLocation(videoId, BANNER), resource(mediaType()))

        assertEquals(3, storageService().storage().size)

        // when
        val actualResult = mediaResourceGateway.getResource(videoId, expectedType)

        // then
        assertNull(actualResult)
    }

    private fun storageService(): InMemoryStorageService =
        storageService as InMemoryStorageService

    private fun getExpectedLocation(
        expectedVideoId: VideoID,
        expectedType: VideoMediaType
    ) = "videoId-${expectedVideoId.value}/type-${expectedType.name}"
}
