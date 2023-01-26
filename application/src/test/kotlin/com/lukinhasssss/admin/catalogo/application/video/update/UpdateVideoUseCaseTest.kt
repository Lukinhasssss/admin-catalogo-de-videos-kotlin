package com.lukinhasssss.admin.catalogo.application.video.update

import com.lukinhasssss.admin.catalogo.application.Fixture
import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.InternalErrorException
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.video.AudioVideoMedia
import com.lukinhasssss.admin.catalogo.domain.video.ImageMedia
import com.lukinhasssss.admin.catalogo.domain.video.MediaResourceGateway
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus
import com.lukinhasssss.admin.catalogo.domain.video.Resource
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Year
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UpdateVideoUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultUpdateVideoUseCase

    @MockK
    private lateinit var videoGateway: VideoGateway

    @MockK
    private lateinit var categoryGateway: CategoryGateway

    @MockK
    private lateinit var castMemberGateway: CastMemberGateway

    @MockK
    private lateinit var genreGateway: GenreGateway

    @MockK
    private lateinit var mediaResourceGateway: MediaResourceGateway

    @Test
    fun givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

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
        val expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO)
        val expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER)
        val expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER)
        val expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL)
        val expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF)

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
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

        every { videoGateway.findById(any()) } returns aVideo
        every { categoryGateway.existsByIds(any()) } returns expectedCategories.toList()
        every { genreGateway.existsByIds(any()) } returns expectedGenres.toList()
        every { castMemberGateway.existsByIds(any()) } returns expectedMembers.toList()
        every { videoGateway.update(any()) } answers { firstArg() }

        mockImageMedia()
        mockAudioVideoMedia()

        // when
        val actualResult = useCase.execute(aCommand)

        // then
        assertNotNull(actualResult)
        assertNotNull(actualResult.id)

        verify { videoGateway.findById(aVideo.id) }

        verify {
            videoGateway.update(
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
                    assertEquals(expectedVideo.name, it.video?.name)
                    assertEquals(expectedTrailer.name, it.trailer?.name)
                    assertEquals(expectedBanner.name, it.banner?.name)
                    assertEquals(expectedThumb.name, it.thumbnail?.name)
                    assertEquals(expectedThumbHalf.name, it.thumbnailHalf?.name)
                    assertEquals(aVideo.createdAt, it.createdAt)
                    assertTrue(aVideo.updatedAt.isBefore(it.updatedAt))
                }
            )
        }
    }

    @Test
    fun givenAValidCommandWithoutCategories_whenCallsUpdateVideo_shouldReturnVideoId() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf<CategoryID>()
        val expectedGenres = setOf(Fixture.Genres.shonen().id)
        val expectedMembers = setOf(Fixture.CastMembers.luffy().id, Fixture.CastMembers.zoro().id)
        val expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO)
        val expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER)
        val expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER)
        val expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL)
        val expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF)

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
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

        every { videoGateway.findById(any()) } returns aVideo
        every { genreGateway.existsByIds(any()) } returns expectedGenres.toList()
        every { castMemberGateway.existsByIds(any()) } returns expectedMembers.toList()
        every { videoGateway.update(any()) } answers { firstArg() }

        mockImageMedia()
        mockAudioVideoMedia()

        // when
        val actualResult = useCase.execute(aCommand)

        // then
        assertNotNull(actualResult)
        assertNotNull(actualResult.id)

        verify { videoGateway.findById(aVideo.id) }

        verify {
            videoGateway.update(
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
                    assertEquals(expectedVideo.name, it.video?.name)
                    assertEquals(expectedTrailer.name, it.trailer?.name)
                    assertEquals(expectedBanner.name, it.banner?.name)
                    assertEquals(expectedThumb.name, it.thumbnail?.name)
                    assertEquals(expectedThumbHalf.name, it.thumbnailHalf?.name)
                    assertEquals(aVideo.createdAt, it.createdAt)
                    assertTrue(aVideo.updatedAt.isBefore(it.updatedAt))
                }
            )
        }
    }

    @Test
    fun givenAValidCommandWithoutGenres_whenCallsUpdateVideo_shouldReturnVideoId() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(Fixture.Categories.animes().id)
        val expectedGenres = setOf<GenreID>()
        val expectedMembers = setOf(Fixture.CastMembers.luffy().id, Fixture.CastMembers.zoro().id)
        val expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO)
        val expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER)
        val expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER)
        val expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL)
        val expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF)

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
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

        every { videoGateway.findById(any()) } returns aVideo
        every { categoryGateway.existsByIds(any()) } returns expectedCategories.toList()
        every { castMemberGateway.existsByIds(any()) } returns expectedMembers.toList()
        every { videoGateway.update(any()) } answers { firstArg() }

        mockImageMedia()
        mockAudioVideoMedia()

        // when
        val actualResult = useCase.execute(aCommand)

        // then
        assertNotNull(actualResult)
        assertNotNull(actualResult.id)

        verify { videoGateway.findById(aVideo.id) }

        verify {
            videoGateway.update(
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
                    assertEquals(expectedVideo.name, it.video?.name)
                    assertEquals(expectedTrailer.name, it.trailer?.name)
                    assertEquals(expectedBanner.name, it.banner?.name)
                    assertEquals(expectedThumb.name, it.thumbnail?.name)
                    assertEquals(expectedThumbHalf.name, it.thumbnailHalf?.name)
                    assertEquals(aVideo.createdAt, it.createdAt)
                    assertTrue(aVideo.updatedAt.isBefore(it.updatedAt))
                }
            )
        }
    }

    @Test
    fun givenAValidCommandWithoutCastMembers_whenCallsUpdateVideo_shouldReturnVideoId() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(Fixture.Categories.animes().id)
        val expectedGenres = setOf(Fixture.Genres.shonen().id)
        val expectedMembers = setOf<CastMemberID>()
        val expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO)
        val expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER)
        val expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER)
        val expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL)
        val expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF)

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
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

        every { videoGateway.findById(any()) } returns aVideo
        every { categoryGateway.existsByIds(any()) } returns expectedCategories.toList()
        every { genreGateway.existsByIds(any()) } returns expectedGenres.toList()
        every { videoGateway.update(any()) } answers { firstArg() }

        mockImageMedia()
        mockAudioVideoMedia()

        // when
        val actualResult = useCase.execute(aCommand)

        // then
        assertNotNull(actualResult)
        assertNotNull(actualResult.id)

        verify { videoGateway.findById(aVideo.id) }

        verify {
            videoGateway.update(
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
                    assertEquals(expectedVideo.name, it.video?.name)
                    assertEquals(expectedTrailer.name, it.trailer?.name)
                    assertEquals(expectedBanner.name, it.banner?.name)
                    assertEquals(expectedThumb.name, it.thumbnail?.name)
                    assertEquals(expectedThumbHalf.name, it.thumbnailHalf?.name)
                    assertEquals(aVideo.createdAt, it.createdAt)
                    assertTrue(aVideo.updatedAt.isBefore(it.updatedAt))
                }
            )
        }
    }

    @Test
    fun givenAValidCommandWithoutResources_whenCallsUpdateVideo_shouldReturnVideoId() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

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

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear.value,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating.name,
            categories = expectedCategories.asString().toSet(),
            genres = expectedGenres.asString().toSet(),
            members = expectedMembers.asString().toSet()
        )

        every { videoGateway.findById(any()) } returns aVideo
        every { categoryGateway.existsByIds(any()) } returns expectedCategories.toList()
        every { genreGateway.existsByIds(any()) } returns expectedGenres.toList()
        every { castMemberGateway.existsByIds(any()) } returns expectedMembers.toList()
        every { videoGateway.update(any()) } answers { firstArg() }

        mockImageMedia()
        mockAudioVideoMedia()

        // when
        val actualResult = useCase.execute(aCommand)

        // then
        assertNotNull(actualResult)
        assertNotNull(actualResult.id)

        verify { videoGateway.findById(aVideo.id) }

        verify {
            videoGateway.update(
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
                    assertNull(it.video)
                    assertNull(it.trailer)
                    assertNull(it.banner)
                    assertNull(it.thumbnail)
                    assertNull(it.thumbnailHalf)
                    assertEquals(aVideo.createdAt, it.createdAt)
                    assertTrue(aVideo.updatedAt.isBefore(it.updatedAt))
                }
            )
        }
    }

    @Test
    fun givenAnEmptyTitle_whenCallsUpdateVideo_shouldReturnNotificationException() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

        val expectedTitle = ""
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf<CategoryID>()
        val expectedGenres = setOf<GenreID>()
        val expectedMembers = setOf<CastMemberID>()

        val expectedErrorCount = 1
        val expectedErrorMessage = "'title' should not be empty"

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear.value,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating.name,
            categories = expectedCategories.asString().toSet(),
            genres = expectedGenres.asString().toSet(),
            members = expectedMembers.asString().toSet()
        )

        every { videoGateway.findById(any()) } returns aVideo

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)

        verify { videoGateway.findById(aVideo.id) }

        verify(exactly = 0) { categoryGateway.existsByIds(any()) }
        verify(exactly = 0) { genreGateway.existsByIds(any()) }
        verify(exactly = 0) { castMemberGateway.existsByIds(any()) }
        verify(exactly = 0) { mediaResourceGateway.storeAudioVideo(any(), any()) }
        verify(exactly = 0) { mediaResourceGateway.storeImage(any(), any()) }
        verify(exactly = 0) { videoGateway.update(any()) }
    }

    @Test
    fun givenATitleWithLengthGreaterThan255_whenCallsUpdateVideo_shouldReturnNotificationException() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

        val expectedTitle = "t".repeat(256)
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf<CategoryID>()
        val expectedGenres = setOf<GenreID>()
        val expectedMembers = setOf<CastMemberID>()

        val expectedErrorCount = 1
        val expectedErrorMessage = "'title' must be between 1 and 255 characters"

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear.value,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating.name,
            categories = expectedCategories.asString().toSet(),
            genres = expectedGenres.asString().toSet(),
            members = expectedMembers.asString().toSet()
        )

        every { videoGateway.findById(any()) } returns aVideo

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)

        verify { videoGateway.findById(aVideo.id) }

        verify(exactly = 0) { categoryGateway.existsByIds(any()) }
        verify(exactly = 0) { genreGateway.existsByIds(any()) }
        verify(exactly = 0) { castMemberGateway.existsByIds(any()) }
        verify(exactly = 0) { mediaResourceGateway.storeAudioVideo(any(), any()) }
        verify(exactly = 0) { mediaResourceGateway.storeImage(any(), any()) }
        verify(exactly = 0) { videoGateway.update(any()) }
    }

    @Test
    fun givenAnEmptyDescription_whenCallsUpdateVideo_shouldReturnNotificationException() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

        val expectedTitle = Fixture.title()
        val expectedDescription = ""
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf<CategoryID>()
        val expectedGenres = setOf<GenreID>()
        val expectedMembers = setOf<CastMemberID>()

        val expectedErrorCount = 1
        val expectedErrorMessage = "'description' should not be empty"

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear.value,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating.name,
            categories = expectedCategories.asString().toSet(),
            genres = expectedGenres.asString().toSet(),
            members = expectedMembers.asString().toSet()
        )

        every { videoGateway.findById(any()) } returns aVideo

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)

        verify { videoGateway.findById(aVideo.id) }

        verify(exactly = 0) { categoryGateway.existsByIds(any()) }
        verify(exactly = 0) { genreGateway.existsByIds(any()) }
        verify(exactly = 0) { castMemberGateway.existsByIds(any()) }
        verify(exactly = 0) { mediaResourceGateway.storeAudioVideo(any(), any()) }
        verify(exactly = 0) { mediaResourceGateway.storeImage(any(), any()) }
        verify(exactly = 0) { videoGateway.update(any()) }
    }

    @Test
    fun givenADescriptionWithLengthGreaterThan4000_whenCallsUpdateVideo_shouldReturnNotificationException() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

        val expectedTitle = Fixture.title()
        val expectedDescription = "d".repeat(4001)
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf<CategoryID>()
        val expectedGenres = setOf<GenreID>()
        val expectedMembers = setOf<CastMemberID>()

        val expectedErrorCount = 1
        val expectedErrorMessage = "'description' must be between 1 and 4000 characters"

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear.value,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating.name,
            categories = expectedCategories.asString().toSet(),
            genres = expectedGenres.asString().toSet(),
            members = expectedMembers.asString().toSet()
        )

        every { videoGateway.findById(any()) } returns aVideo

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)

        verify { videoGateway.findById(aVideo.id) }

        verify(exactly = 0) { categoryGateway.existsByIds(any()) }
        verify(exactly = 0) { genreGateway.existsByIds(any()) }
        verify(exactly = 0) { castMemberGateway.existsByIds(any()) }
        verify(exactly = 0) { mediaResourceGateway.storeAudioVideo(any(), any()) }
        verify(exactly = 0) { mediaResourceGateway.storeImage(any(), any()) }
        verify(exactly = 0) { videoGateway.update(any()) }
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateVideoAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

        val animesId = Fixture.Categories.animes().id

        val expectedErrorCount = 1
        val expectedErrorMessage = "Some categories could not be found: ${animesId.value}"

        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(animesId)
        val expectedGenres = setOf(Fixture.Genres.shonen().id)
        val expectedMembers = setOf(Fixture.CastMembers.luffy().id, Fixture.CastMembers.zoro().id)

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear.value,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating.name,
            categories = expectedCategories.asString().toSet(),
            genres = expectedGenres.asString().toSet(),
            members = expectedMembers.asString().toSet()
        )

        every { videoGateway.findById(any()) } returns aVideo
        every { categoryGateway.existsByIds(any()) } returns emptyList()
        every { genreGateway.existsByIds(any()) } returns expectedGenres.toList()
        every { castMemberGateway.existsByIds(any()) } returns expectedMembers.toList()
        every { videoGateway.update(any()) } answers { firstArg() }

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)

        verify(exactly = 1) { categoryGateway.existsByIds(any()) }
        verify(exactly = 1) { genreGateway.existsByIds(any()) }
        verify(exactly = 1) { castMemberGateway.existsByIds(any()) }
        verify(exactly = 0) { mediaResourceGateway.storeAudioVideo(any(), any()) }
        verify(exactly = 0) { mediaResourceGateway.storeImage(any(), any()) }
        verify(exactly = 0) { videoGateway.create(any()) }
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateVideoAndSomeGenresDoesNotExists_shouldReturnNotificationException() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

        val shonenId = Fixture.Genres.shonen().id

        val expectedErrorCount = 1
        val expectedErrorMessage = "Some genres could not be found: ${shonenId.value}"

        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(Fixture.Categories.animes().id)
        val expectedGenres = setOf(shonenId)
        val expectedMembers = setOf(Fixture.CastMembers.luffy().id, Fixture.CastMembers.zoro().id)

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear.value,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating.name,
            categories = expectedCategories.asString().toSet(),
            genres = expectedGenres.asString().toSet(),
            members = expectedMembers.asString().toSet()
        )

        every { videoGateway.findById(any()) } returns aVideo
        every { categoryGateway.existsByIds(any()) } returns expectedCategories.toList()
        every { genreGateway.existsByIds(any()) } returns emptyList()
        every { castMemberGateway.existsByIds(any()) } returns expectedMembers.toList()
        every { videoGateway.create(any()) } answers { firstArg() }

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)

        verify(exactly = 1) { categoryGateway.existsByIds(expectedCategories) }
        verify(exactly = 1) { genreGateway.existsByIds(expectedGenres) }
        verify(exactly = 1) { castMemberGateway.existsByIds(expectedMembers) }
        verify(exactly = 0) { mediaResourceGateway.storeAudioVideo(any(), any()) }
        verify(exactly = 0) { mediaResourceGateway.storeImage(any(), any()) }
        verify(exactly = 0) { videoGateway.create(any()) }
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateVideoAndSomeCastMembersDoesNotExists_shouldReturnNotificationException() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

        val luffyId = Fixture.CastMembers.luffy().id

        val expectedErrorCount = 1
        val expectedErrorMessage = "Some cast members could not be found: ${luffyId.value}"

        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(Fixture.Categories.animes().id)
        val expectedGenres = setOf(Fixture.Genres.shonen().id)
        val expectedMembers = setOf(luffyId)

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear.value,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating.name,
            categories = expectedCategories.asString().toSet(),
            genres = expectedGenres.asString().toSet(),
            members = expectedMembers.asString().toSet()
        )

        every { videoGateway.findById(any()) } returns aVideo
        every { categoryGateway.existsByIds(any()) } returns expectedCategories.toList()
        every { genreGateway.existsByIds(any()) } returns expectedGenres.toList()
        every { castMemberGateway.existsByIds(any()) } returns emptyList()
        every { videoGateway.create(any()) } answers { firstArg() }

        // when
        val actualException = assertThrows<NotificationException> { useCase.execute(aCommand) }

        // then

        // then
        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)

        verify(exactly = 1) { categoryGateway.existsByIds(expectedCategories) }
        verify(exactly = 1) { genreGateway.existsByIds(expectedGenres) }
        verify(exactly = 1) { castMemberGateway.existsByIds(expectedMembers) }
        verify(exactly = 0) { mediaResourceGateway.storeAudioVideo(any(), any()) }
        verify(exactly = 0) { mediaResourceGateway.storeImage(any(), any()) }
        verify(exactly = 0) { videoGateway.create(any()) }
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateVideoThrowsException_shouldNotCallsClearResources() {
        // given
        val aVideo = Fixture.Videos.systemDesign()

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
        val expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO)
        val expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER)
        val expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER)
        val expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL)
        val expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF)

        val expectedErrorMessage = "An error on update video was observed [videoID:"

        val aCommand = UpdateVideoCommand.with(
            anId = aVideo.id.value,
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

        every { videoGateway.findById(any()) } returns aVideo
        every { categoryGateway.existsByIds(any()) } returns expectedCategories.toList()
        every { genreGateway.existsByIds(any()) } returns expectedGenres.toList()
        every { castMemberGateway.existsByIds(any()) } returns expectedMembers.toList()
        every { videoGateway.update(any()) } throws RuntimeException("Internal Server Error")

        mockImageMedia()
        mockAudioVideoMedia()

        every { mediaResourceGateway.clearResources(any()) } returns Unit

        // when
        val actualException = assertThrows<InternalErrorException> { useCase.execute(aCommand) }

        // then
        assertNotNull(actualException)
        assertTrue(actualException.message.startsWith(expectedErrorMessage))

        verify(exactly = 0) { mediaResourceGateway.clearResources(any()) }
    }

    private fun mockImageMedia() =
        every { mediaResourceGateway.storeImage(any(), any()) } answers {
            val resource = this.secondArg<Resource>()

            ImageMedia.with(
                checksum = UUID.randomUUID().toString(),
                name = resource.name,
                location = "/img"
            )
        }

    private fun mockAudioVideoMedia() =
        every { mediaResourceGateway.storeAudioVideo(any(), any()) } answers {
            val resource = this.secondArg<Resource>()

            AudioVideoMedia.with(
                checksum = UUID.randomUUID().toString(),
                name = resource.name,
                rawLocation = "/img",
                encodedLocation = "",
                status = MediaStatus.PENDING
            )
        }
}