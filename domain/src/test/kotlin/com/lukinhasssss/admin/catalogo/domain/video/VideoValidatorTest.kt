package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.DomainException
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.validation.handler.ThrowsValidationHandler
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Year
import kotlin.test.assertEquals

class VideoValidatorTest {

    @Test
    fun givenEmptyTitle_whenCallsValidate_shouldReceiveError() {
        // given
        val expectedTitle = ""
        val expectedDescription = "Video Description"
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())

        val expectedErrorCount = 1
        val expectedErrorMessage = "'title' should not be empty"

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

        // when
        val actualError = assertThrows<DomainException> {
            actualVideo.validate(ThrowsValidationHandler())
        }

        // then
        assertEquals(expectedErrorCount, actualError.errors.size)
        assertEquals(expectedErrorMessage, actualError.errors[0].message)
    }

    @Test
    fun givenTitleWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
        // given
        val expectedTitle = "t".repeat(256)
        val expectedDescription = "Video Description"
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())

        val expectedErrorCount = 1
        val expectedErrorMessage = "'title' must be between 1 and 255 characters"

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

        // when
        val actualError = assertThrows<DomainException> {
            actualVideo.validate(ThrowsValidationHandler())
        }

        // then
        assertEquals(expectedErrorCount, actualError.errors.size)
        assertEquals(expectedErrorMessage, actualError.errors[0].message)
    }

    @Test
    fun givenEmptyDescription_whenCallsValidate_shouldReceiveError() {
        // given
        val expectedTitle = "Video Title"
        val expectedDescription = ""
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())

        val expectedErrorCount = 1
        val expectedErrorMessage = "'description' should not be empty"

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

        // when
        val actualError = assertThrows<DomainException> {
            actualVideo.validate(ThrowsValidationHandler())
        }

        // then
        assertEquals(expectedErrorCount, actualError.errors.size)
        assertEquals(expectedErrorMessage, actualError.errors[0].message)
    }

    @Test
    fun givenDescriptionWithLengthGreaterThan4000_whenCallsValidate_shouldReceiveError() {
        // given
        val expectedTitle = "Video Title"
        val expectedDescription = "d".repeat(4001)
        val expectedLaunchedAt = Year.of(2022)
        val expectedDuration = 120.10
        val expectedOpened = false
        val expectedPublished = false
        val expectedRating = Rating.L
        val expectedCategories = setOf(CategoryID.unique())
        val expectedGenres = setOf(GenreID.unique())
        val expectedMembers = setOf(CastMemberID.unique())

        val expectedErrorCount = 1
        val expectedErrorMessage = "'description' must be between 1 and 4000 characters"

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

        // when
        val actualError = assertThrows<DomainException> {
            actualVideo.validate(ThrowsValidationHandler())
        }

        // then
        assertEquals(expectedErrorCount, actualError.errors.size)
        assertEquals(expectedErrorMessage, actualError.errors[0].message)
    }
}
