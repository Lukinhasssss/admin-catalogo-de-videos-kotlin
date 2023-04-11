package com.lukinhasssss.admin.catalogo.infrastructure.services.impl

import com.google.api.gax.paging.Page
import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.Storage
import com.google.cloud.storage.Storage.BlobListOption.prefix
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.resource.Resource
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.BANNER
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.VIDEO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GoogleCloudStorageServiceTest {

    private lateinit var target: GoogleCloudStorageService

    private lateinit var storage: Storage

    private val bucket = "codeflix_test"

    @BeforeEach
    fun setUp() {
        storage = mockk()
        target = GoogleCloudStorageService(bucket, storage)
    }

    @Test
    fun givenAValidResource_whenCallsStore_shouldPersistIt() {
        // given
        val expectedName = IdUtils.uuid()
        val expectedResource = Fixture.Videos.resource(VIDEO)

        val blob = mockBlob(expectedName, expectedResource)

        every { storage.create(any(), any<ByteArray>()) } returns blob

        // when
        target.store(expectedName, expectedResource)

        // then
        verify {
            storage.create(
                withArg {
                    assertEquals(bucket, it.blobId.bucket)
                    assertEquals(expectedName, it.blobId.name)
                    assertEquals(expectedName, it.name)
                    assertEquals(expectedResource.checksum, it.crc32cToHexString)
                    assertEquals(expectedResource.contentType, it.contentType)
                },
                expectedResource.content
            )
        }
    }

    @Test
    fun givenAValidResource_whenCallsGet_shouldRetrieveIt() {
        // given
        val expectedName = IdUtils.uuid()
        val expectedResource = Fixture.Videos.resource(VIDEO)

        val blob = mockBlob(expectedName, expectedResource)

        every { storage.get(any(), any<String>()) } returns blob

        // when
        val actualResource = target.get(expectedName)!!

        // then
        verify { storage.get(bucket, expectedName) }

        assertEquals(expectedResource, actualResource)
    }

    @Test
    fun givenAnInvalidResource_whenCallsGet_shouldReturnNull() {
        // given
        val expectedName = IdUtils.uuid()

        every { storage.get(any(), any<String>()) } returns null

        // when
        val actualResource = target.get(expectedName)

        // then
        verify { storage.get(bucket, expectedName) }

        assertNull(actualResource)
    }

    @Test
    fun givenAValidPrefix_whenCallsList_shouldRetrieveAll() {
        // given
        val expectedPrefix = "media_"

        val expectedNameVideo = "media_${IdUtils.uuid()}"
        val expectedVideo = Fixture.Videos.resource(VIDEO)

        val expectedNameBanner = "media_${IdUtils.uuid()}"
        val expectedBanner = Fixture.Videos.resource(BANNER)

        val expectedResources = listOf(expectedNameBanner, expectedNameVideo)

        val blobVideo = mockBlob(expectedNameVideo, expectedVideo)
        val blobBanner = mockBlob(expectedNameBanner, expectedBanner)

        val page = mockk<Page<Blob>>()

        every { page.iterateAll() } returns listOf(blobVideo, blobBanner)
        every { storage.list(any<String>(), any()) } returns page

        // when
        val actualResource = target.list(expectedPrefix)

        // then
        verify { storage.list(bucket, prefix(expectedPrefix)) }

        assertTrue(expectedResources.size == actualResource.size && expectedResources.containsAll(actualResource))
    }

    @Test
    fun givenValidNames_whenCallsDelete_shouldDeleteAll() {
        // given
        val expectedNameVideo = "media_${IdUtils.uuid()}"
        val expectedNameBanner = "media_${IdUtils.uuid()}"
        val expectedResources = listOf(expectedNameBanner, expectedNameVideo)

        every { storage.delete(any<List<BlobId>>()) } returns any()

        // when
        target.deleteAll(expectedResources)

        // then
        verify {
            storage.delete(
                withArg { blobIds: ArrayList<BlobId> ->
                    val actualResources = blobIds.map { it.name }

                    assertTrue(expectedResources.size == actualResources.size && expectedResources.containsAll(actualResources))
                }
            )
        }
    }

    private fun mockBlob(name: String, expectedResource: Resource) = with(expectedResource) {
        val blob = mockk<Blob>()

        every { blob.blobId } returns BlobId.of(bucket, name)
        every { blob.crc32cToHexString } returns checksum
        every { blob.getContent() } returns content
        every { blob.contentType } returns contentType
        every { blob.name } returns expectedResource.name

        blob
    }
}
