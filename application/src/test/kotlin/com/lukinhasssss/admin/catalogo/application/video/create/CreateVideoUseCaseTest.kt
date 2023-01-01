package com.lukinhasssss.admin.catalogo.application.video.create

import com.lukinhasssss.admin.catalogo.application.Fixture
import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.video.Resource.Type
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.Year
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CreateVideoUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultCreateVideoUseCase

    @MockK
    private lateinit var videoGateway: VideoGateway

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @MockK
    private lateinit var castMemberGateway: CastMemberGateway

    @MockK
    private lateinit var genreGateway: GenreGateway

    @Test
    fun givenAValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {
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
        val expectedVideo = Fixture.Videos.resource(Type.VIDEO)
        val expectedTrailer = Fixture.Videos.resource(Type.TRAILER)
        val expectedBanner = Fixture.Videos.resource(Type.BANNER)
        val expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL)
        val expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF)

        val aCommand = CreateVideoCommand.with(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear.value,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating.name,
            categories = expectedCategories.asString().toSet(),
            genres = expectedGenres.asString().toSet(),
            members = expectedMembers.asString().toSet(),
            aVideo = expectedVideo,
            aTrailer = expectedTrailer,
            aBanner = expectedBanner,
            aThumbnail = expectedThumb,
            aThumbnailHalf = expectedThumbHalf
        )

        every { categoryGateway.existsByIds(any()) } returns expectedCategories.toList()
        every { castMemberGateway.existsByIds(any()) } returns expectedMembers.toList()
        every { genreGateway.existsByIds(any()) } returns expectedGenres.toList()
        every { videoGateway.create(any()) } answers { firstArg() }

        // when
        val actualResult = useCase.execute(aCommand)

        // then
        assertNotNull(actualResult)
        assertNotNull(actualResult.id)

        verify {
            videoGateway.create(
                withArg {
                    assertEquals(expectedTitle, it.title)
                    assertEquals(expectedDescription, it.description)
                    assertEquals(expectedLaunchYear, it.launchedAt)
                    assertEquals(expectedDuration, it.duration)
                    assertEquals(expectedOpened, it.opened)
                    assertEquals(expectedPublished, it.published)
                    assertEquals(expectedRating, it.rating)
                    assertEquals(expectedCategories, it.categories)
                    assertEquals(expectedGenres, it.genres)
                    assertEquals(expectedMembers, it.castMembers)
                    // assertEquals(expectedVideo.name, it.video?.name)
                    // assertEquals(expectedTrailer.name, it.trailer?.name)
                    // assertEquals(expectedBanner.name, it.banner?.name)
                    // assertEquals(expectedThumb.name, it.thumbnail?.name)
                    // assertEquals(expectedThumbHalf.name, it.thumbnailHalf?.name)
                }
            )
        }
    }
}
