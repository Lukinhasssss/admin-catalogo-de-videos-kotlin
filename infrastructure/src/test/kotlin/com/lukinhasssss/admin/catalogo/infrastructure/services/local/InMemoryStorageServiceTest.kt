package com.lukinhasssss.admin.catalogo.infrastructure.services.local

import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType.VIDEO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InMemoryStorageServiceTest {

    private val target = InMemoryStorageService()

    @BeforeEach
    fun setUp() = target.reset()

    @Test
    fun givenAValidResource_whenCallsStore_shouldStoreIt() {
        // given
        val expectedName = IdUtils.uuid()
        val expectedResource = Fixture.Videos.resource(VIDEO)

        // when
        target.store(expectedName, expectedResource)

        // then
        assertEquals(expectedResource, target.storage()[expectedName])
    }

    @Test
    fun givenAValidResource_whenCallsGet_shouldRetrieveIt() {
        // given
        val expectedName = IdUtils.uuid()
        val expectedResource = Fixture.Videos.resource(VIDEO)

        target.storage()[expectedName] = expectedResource

        // when
        val actualResource = target.get(expectedName)

        // then
        assertEquals(expectedResource, actualResource)
    }

    @Test
    fun givenAnInvalidResource_whenCallsGet_shouldReturnNull() {
        // given
        val expectedName = IdUtils.uuid()

        // when
        val actualResource = target.get(expectedName)

        // then
        assertNull(actualResource)
    }

    @Test
    fun givenAValidPrefix_whenCallsList_shouldRetrieveAll() {
        // given
        val expectedNames = listOf(
            "video_${IdUtils.uuid()}",
            "video_${IdUtils.uuid()}",
            "video_${IdUtils.uuid()}"
        )

        val allNames = mutableListOf("image_${IdUtils.uuid()}", "image_${IdUtils.uuid()}")
        allNames.addAll(expectedNames)

        allNames.forEach { name ->
            target.storage()[name] = Fixture.Videos.resource(VIDEO)
        }

        assertEquals(5, target.storage().size)

        // when
        val actualResource = target.list("video")

        // then
        assertTrue(expectedNames.size == actualResource.size && expectedNames.containsAll(actualResource))
    }

    @Test
    fun givenValidNames_whenCallsDelete_shouldDeleteAll() {
        // given
        val videos = listOf(
            "video_${IdUtils.uuid()}",
            "video_${IdUtils.uuid()}",
            "video_${IdUtils.uuid()}"
        )

        val expectedNames = setOf(
            "image_${IdUtils.uuid()}",
            "image_${IdUtils.uuid()}"
        )

        val allNames = ArrayList(videos)
        allNames.addAll(expectedNames)

        allNames.forEach { name ->
            target.storage()[name] = Fixture.Videos.resource(VIDEO)
        }

        assertEquals(5, target.storage().size)

        // when
        target.deleteAll(videos)

        // then
        assertEquals(2, target.storage().size)
        val teste = target.storage().values
        assertEquals(expectedNames, target.storage().keys)
    }
}
