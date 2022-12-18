package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.validation.handler.ThrowsValidationHandler
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Year
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class VideoTest {

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
        }

        assertDoesNotThrow { actualVideo.validate(ThrowsValidationHandler()) }
    }
}
