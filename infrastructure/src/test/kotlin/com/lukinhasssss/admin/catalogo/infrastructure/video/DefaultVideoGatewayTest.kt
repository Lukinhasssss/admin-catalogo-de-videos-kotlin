package com.lukinhasssss.admin.catalogo.infrastructure.video

import com.lukinhasssss.admin.catalogo.IntegrationTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMember
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberGateway
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.Category
import com.lukinhasssss.admin.catalogo.domain.category.CategoryGateway
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.genre.Genre
import com.lukinhasssss.admin.catalogo.domain.genre.GenreGateway
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.video.AudioVideoMedia
import com.lukinhasssss.admin.catalogo.domain.video.ImageMedia
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoSearchQuery
import com.lukinhasssss.admin.catalogo.infrastructure.video.persistence.VideoRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.Year
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@IntegrationTest
class DefaultVideoGatewayTest {

    @Autowired
    private lateinit var videoGateway: DefaultVideoGateway

    @Autowired
    private lateinit var castMemberGateway: CastMemberGateway

    @Autowired
    private lateinit var categoryGateway: CategoryGateway

    @Autowired
    private lateinit var genreGateway: GenreGateway

    @Autowired
    private lateinit var videoRepository: VideoRepository

    private lateinit var zoro: CastMember
    private lateinit var theRock: CastMember

    private lateinit var animes: Category
    private lateinit var filmes: Category

    private lateinit var shonen: Genre
    private lateinit var acao: Genre

    @BeforeEach
    fun setUp() {
        zoro = castMemberGateway.create(Fixture.CastMembers.zoro())
        theRock = castMemberGateway.create(Fixture.CastMembers.theRock())

        animes = categoryGateway.create(Fixture.Categories.animes())
        filmes = categoryGateway.create(Fixture.Categories.filmes())

        shonen = genreGateway.create(Fixture.Genres.shonen())
        acao = genreGateway.create(Fixture.Genres.acao())
    }

    @Test
    fun testInjection() {
        assertNotNull(videoGateway)
        assertNotNull(castMemberGateway)
        assertNotNull(categoryGateway)
        assertNotNull(genreGateway)
        assertNotNull(videoRepository)
    }

    @Test
    @Transactional
    fun givenAValidVideo_whenCallsCreate_shouldPersistIt() {
        // given
        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(animes.id)
        val expectedGenres = setOf(shonen.id)
        val expectedMembers = setOf(zoro.id)

        val expectedVideo = AudioVideoMedia.with(checksum = "123", name = "video", rawLocation = "/media/video")
        val expectedTrailer = AudioVideoMedia.with(checksum = "123", name = "trailer", rawLocation = "/media/trailer")
        val expectedBanner = ImageMedia.with(checksum = "123", name = "banner", location = "/media/banner")
        val expectedThumb = ImageMedia.with(checksum = "123", name = "thumb", location = "/media/thumb")
        val expectedThumbHalf = ImageMedia.with(checksum = "123", name = "thumbHalf", location = "/media/thumbHalf")

        val aVideo = Video.newVideo(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        ).copy(
            video = expectedVideo,
            trailer = expectedTrailer,
            banner = expectedBanner,
            thumbnail = expectedThumb,
            thumbnailHalf = expectedThumbHalf
        )

        // when
        val actualVideo = videoGateway.create(aVideo)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertEquals(expectedVideo.name, video?.name)
            assertEquals(expectedTrailer.name, trailer?.name)
            assertEquals(expectedBanner.name, banner?.name)
            assertEquals(expectedThumb.name, thumbnail?.name)
            assertEquals(expectedThumbHalf.name, thumbnailHalf?.name)
        }

        val persistedVideo = videoRepository.findById(actualVideo.id.value).get()

        with(persistedVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, Year.of(yearLaunched))
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, getCategoriesID())
            assertEquals(expectedGenres, getGenresID())
            assertEquals(expectedMembers, getCastMembersID())
            assertEquals(expectedVideo.name, video?.name)
            assertEquals(expectedTrailer.name, trailer?.name)
            assertEquals(expectedBanner.name, banner?.name)
            assertEquals(expectedThumb.name, thumbnail?.name)
            assertEquals(expectedThumbHalf.name, thumbnailHalf?.name)
        }
    }

    @Test
    @Transactional
    fun givenAValidVideoWithoutRelations_whenCallsCreate_shouldPersistIt() {
        // given
        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf<CategoryID>()
        val expectedGenres = setOf<GenreID>()
        val expectedMembers = setOf<CastMemberID>()

        val aVideo = Video.newVideo(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        )

        // when
        val actualVideo = videoGateway.create(aVideo)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertNull(video)
            assertNull(trailer)
            assertNull(banner)
            assertNull(thumbnail)
            assertNull(thumbnailHalf)
        }

        val persistedVideo = videoRepository.findById(actualVideo.id.value).get()

        with(persistedVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, Year.of(yearLaunched))
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, getCategoriesID())
            assertEquals(expectedGenres, getGenresID())
            assertEquals(expectedMembers, getCastMembersID())
            assertNull(video)
            assertNull(trailer)
            assertNull(banner)
            assertNull(thumbnail)
            assertNull(thumbnailHalf)
        }
    }

    @Test
    @Transactional
    fun givenAValidVideo_whenCallsUpdate_shouldPersistIt() {
        // given
        val aVideo = videoGateway.create(
            Video.newVideo(
                aTitle = Fixture.title(),
                aDescription = Fixture.Videos.description(),
                aLaunchYear = Year.of(Fixture.year()),
                aDuration = Fixture.duration(),
                wasOpened = Fixture.bool(),
                wasPublished = Fixture.bool(),
                aRating = Fixture.Videos.rating(),
                categories = emptySet(),
                genres = emptySet(),
                members = emptySet()
            )
        )

        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(animes.id)
        val expectedGenres = setOf(shonen.id)
        val expectedMembers = setOf(zoro.id)

        val expectedVideo = AudioVideoMedia.with(checksum = "123", name = "video", rawLocation = "/media/video")
        val expectedTrailer = AudioVideoMedia.with(checksum = "123", name = "trailer", rawLocation = "/media/trailer")
        val expectedBanner = ImageMedia.with(checksum = "123", name = "banner", location = "/media/banner")
        val expectedThumb = ImageMedia.with(checksum = "123", name = "thumb", location = "/media/thumb")
        val expectedThumbHalf = ImageMedia.with(checksum = "123", name = "thumbHalf", location = "/media/thumbHalf")

        val aVideoUpdated = aVideo.update(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        ).copy(
            video = expectedVideo,
            trailer = expectedTrailer,
            banner = expectedBanner,
            thumbnail = expectedThumb,
            thumbnailHalf = expectedThumbHalf
        )

        // when
        val actualVideo = videoGateway.update(aVideoUpdated)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertEquals(expectedVideo.name, video?.name)
            assertEquals(expectedTrailer.name, trailer?.name)
            assertEquals(expectedBanner.name, banner?.name)
            assertEquals(expectedThumb.name, thumbnail?.name)
            assertEquals(expectedThumbHalf.name, thumbnailHalf?.name)
            assertNotNull(createdAt)
            assertTrue(aVideo.updatedAt.isBefore(updatedAt))
        }

        val persistedVideo = videoRepository.findById(actualVideo.id.value).get()

        with(persistedVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, Year.of(yearLaunched))
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, getCategoriesID())
            assertEquals(expectedGenres, getGenresID())
            assertEquals(expectedMembers, getCastMembersID())
            assertEquals(expectedVideo.name, video?.name)
            assertEquals(expectedTrailer.name, trailer?.name)
            assertEquals(expectedBanner.name, banner?.name)
            assertEquals(expectedThumb.name, thumbnail?.name)
            assertEquals(expectedThumbHalf.name, thumbnailHalf?.name)
            assertNotNull(createdAt)
            assertTrue(aVideo.updatedAt.isBefore(updatedAt))
        }
    }

    @Test
    fun givenAValidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        val aVideo = videoGateway.create(
            Video.newVideo(
                aTitle = Fixture.title(),
                aDescription = Fixture.Videos.description(),
                aLaunchYear = Year.of(Fixture.year()),
                aDuration = Fixture.duration(),
                wasOpened = Fixture.bool(),
                wasPublished = Fixture.bool(),
                aRating = Fixture.Videos.rating(),
                categories = emptySet(),
                genres = emptySet(),
                members = emptySet()
            )
        )

        assertEquals(1, videoRepository.count())

        val anId = aVideo.id

        // when
        videoGateway.deleteById(anId)

        // then
        assertEquals(0, videoRepository.count())
    }

    @Test
    fun givenAnInvalidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        videoGateway.create(
            Video.newVideo(
                aTitle = Fixture.title(),
                aDescription = Fixture.Videos.description(),
                aLaunchYear = Year.of(Fixture.year()),
                aDuration = Fixture.duration(),
                wasOpened = Fixture.bool(),
                wasPublished = Fixture.bool(),
                aRating = Fixture.Videos.rating(),
                categories = emptySet(),
                genres = emptySet(),
                members = emptySet()
            )
        )

        assertEquals(1, videoRepository.count())

        val anId = VideoID.unique()

        // when
        videoGateway.deleteById(anId)

        // then
        assertEquals(1, videoRepository.count())
    }

    @Test
    fun givenAValidVideo_whenCallsFindById_shouldReturnIt() {
        // given
        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(animes.id)
        val expectedGenres = setOf(shonen.id)
        val expectedMembers = setOf(zoro.id)

        val expectedVideo = AudioVideoMedia.with(checksum = "123", name = "video", rawLocation = "/media/video")
        val expectedTrailer = AudioVideoMedia.with(checksum = "123", name = "trailer", rawLocation = "/media/trailer")
        val expectedBanner = ImageMedia.with(checksum = "123", name = "banner", location = "/media/banner")
        val expectedThumb = ImageMedia.with(checksum = "123", name = "thumb", location = "/media/thumb")
        val expectedThumbHalf = ImageMedia.with(checksum = "123", name = "thumbHalf", location = "/media/thumbHalf")

        val aVideo = videoGateway.create(
            Video.newVideo(
                aTitle = expectedTitle,
                aDescription = expectedDescription,
                aLaunchYear = expectedLaunchYear,
                aDuration = expectedDuration,
                wasOpened = expectedOpened,
                wasPublished = expectedPublished,
                aRating = expectedRating,
                categories = expectedCategories,
                genres = expectedGenres,
                members = expectedMembers
            ).copy(
                video = expectedVideo,
                trailer = expectedTrailer,
                banner = expectedBanner,
                thumbnail = expectedThumb,
                thumbnailHalf = expectedThumbHalf
            )
        )

        // when
        val actualVideo = videoGateway.findById(aVideo.id)!!

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertEquals(expectedVideo.name, video?.name)
            assertEquals(expectedTrailer.name, trailer?.name)
            assertEquals(expectedBanner.name, banner?.name)
            assertEquals(expectedThumb.name, thumbnail?.name)
            assertEquals(expectedThumbHalf.name, thumbnailHalf?.name)
        }
    }

    @Test
    fun givenAValidVideoWithouRelations_whenCallsFindById_shouldReturnIt() {
        // given
        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf<CategoryID>()
        val expectedGenres = setOf<GenreID>()
        val expectedMembers = setOf<CastMemberID>()

        val aVideo = videoGateway.create(
            Video.newVideo(
                aTitle = expectedTitle,
                aDescription = expectedDescription,
                aLaunchYear = expectedLaunchYear,
                aDuration = expectedDuration,
                wasOpened = expectedOpened,
                wasPublished = expectedPublished,
                aRating = expectedRating,
                categories = expectedCategories,
                genres = expectedGenres,
                members = expectedMembers
            )
        )

        // when
        val actualVideo = videoGateway.findById(aVideo.id)!!

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedTitle, title)
            assertEquals(expectedDescription, description)
            assertEquals(expectedLaunchYear, launchedAt)
            assertEquals(expectedDuration, duration)
            assertEquals(expectedOpened, opened)
            assertEquals(expectedPublished, published)
            assertEquals(expectedRating, rating)
            assertEquals(expectedCategories, categories)
            assertEquals(expectedGenres, genres)
            assertEquals(expectedMembers, castMembers)
            assertNull(video)
            assertNull(trailer)
            assertNull(banner)
            assertNull(thumbnail)
            assertNull(thumbnailHalf)
        }
    }

    @Test
    fun givenAnInvalidVideoId_whenCallsFindById_shouldReturnNull() {
        // given
        videoGateway.create(
            Video.newVideo(
                aTitle = Fixture.title(),
                aDescription = Fixture.Videos.description(),
                aLaunchYear = Year.of(Fixture.year()),
                aDuration = Fixture.duration(),
                wasOpened = Fixture.bool(),
                wasPublished = Fixture.bool(),
                aRating = Fixture.Videos.rating(),
                categories = emptySet(),
                genres = emptySet(),
                members = emptySet()
            )
        )

        assertEquals(1, videoRepository.count())

        val anId = VideoID.unique()

        // when
        val actualVideo = videoGateway.findById(anId)

        // then
        assertNull(actualVideo)

        assertEquals(1, videoRepository.count())
    }

    @Test
    fun givenEmptyParams_whenCallsFindAll_shouldReturnAllList() {
        // given
        mockVideos()

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "title"
        val expectedDirection = "asc"
        val expectedTotal = 4

        val aQuery = VideoSearchQuery(
            page = expectedPage,
            perPage = expectedPerPage,
            terms = expectedTerms,
            sort = expectedSort,
            direction = expectedDirection,
            castMembers = emptySet(),
            categories = emptySet(),
            genres = emptySet()
        )

        // when
        val actualPage = videoGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total.toInt())
            assertEquals(expectedTotal, items.size)
        }
    }

    @Test
    fun givenEmptyVideos_whenCallsFindAll_shouldReturnEmptyList() {
        // given
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "title"
        val expectedDirection = "asc"
        val expectedTotal = 0

        val aQuery = VideoSearchQuery(
            page = expectedPage,
            perPage = expectedPerPage,
            terms = expectedTerms,
            sort = expectedSort,
            direction = expectedDirection,
            castMembers = emptySet(),
            categories = emptySet(),
            genres = emptySet()
        )

        // when
        val actualPage = videoGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total.toInt())
            assertEquals(expectedTotal, items.size)
        }
    }

    /*@Test
    fun givenAValidCastMember_whenCallsFindAll_shouldReturnFilteredList() {
        // given
        mockVideos()

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "title"
        val expectedDirection = "asc"
        val expectedTotal = 2

        val aQuery = VideoSearchQuery(
            page = expectedPage,
            perPage = expectedPerPage,
            terms = expectedTerms,
            sort = expectedSort,
            direction = expectedDirection,
            castMembers = setOf(zoro.id),
            categories = emptySet(),
            genres = emptySet()
        )

        // when
        val actualPage = videoGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total.toInt())
            assertEquals(expectedTotal, items.size)

            assertEquals("Aula de empreendedorismo", items[0].title)
            assertEquals("System Design no Mercado Livre na prática", items[1].title)
        }
    }

    @Test
    fun givenAValidCategory_whenCallsFindAll_shouldReturnFilteredList() {
        // given
        mockVideos()

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "title"
        val expectedDirection = "asc"
        val expectedTotal = 2

        val aQuery = VideoSearchQuery(
            page = expectedPage,
            perPage = expectedPerPage,
            terms = expectedTerms,
            sort = expectedSort,
            direction = expectedDirection,
            castMembers = emptySet(),
            categories = setOf(animes.id),
            genres = emptySet()
        )

        // when
        val actualPage = videoGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total.toInt())
            assertEquals(expectedTotal, items.size)

            assertEquals("21.1 Implementação dos testes integrados do findAll", items[0].title)
            assertEquals("Aula de empreendedorismo", items[1].title)
        }
    }

    @Test
    fun givenAValidGenre_whenCallsFindAll_shouldReturnFilteredList() {
        // given
        mockVideos()

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "title"
        val expectedDirection = "asc"
        val expectedTotal = 1

        val aQuery = VideoSearchQuery(
            page = expectedPage,
            perPage = expectedPerPage,
            terms = expectedTerms,
            sort = expectedSort,
            direction = expectedDirection,
            castMembers = emptySet(),
            categories = emptySet(),
            genres = setOf(shonen.id)
        )

        // when
        val actualPage = videoGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total.toInt())
            assertEquals(expectedTotal, items.size)

            assertEquals("Aula de empreendedorismo", items[0].title)
        }
    }

    @Test
    fun givenAllParameters_whenCallsFindAll_shouldReturnFilteredList() {
        // given
        mockVideos()

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "title"
        val expectedDirection = "asc"
        val expectedTotal = 1

        val aQuery = VideoSearchQuery(
            page = expectedPage,
            perPage = expectedPerPage,
            terms = expectedTerms,
            sort = expectedSort,
            direction = expectedDirection,
            castMembers = setOf(zoro.id),
            categories = setOf(animes.id),
            genres = setOf(shonen.id)
        )

        // when
        val actualPage = videoGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total.toInt())
            assertEquals(expectedTotal, items.size)

            assertEquals("Aula de empreendedorismo", items[0].title)
        }
    }*/

    @ParameterizedTest
    @CsvSource(
        "system, 0, 10, 1, 1, System Design no Mercado Livre na prática",
        "microsser, 0, 10, 1, 1, Não cometa esses erros ao trabalhar com Microsserviços",
        "empreendedorismo, 0, 10, 1, 1, Aula de empreendedorismo",
        "21, 0, 10, 1, 1, 21.1 Implementação dos testes integrados do findAll",
    )
    fun givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
        expectedTerms: String,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedVideo: String
    ) {
        // given
        mockVideos()

        val expectedSort = "title"
        val expectedDirection = "asc"

        val aQuery = VideoSearchQuery(
            page = expectedPage,
            perPage = expectedPerPage,
            terms = expectedTerms,
            sort = expectedSort,
            direction = expectedDirection,
            castMembers = emptySet(),
            categories = emptySet(),
            genres = emptySet()
        )

        // when
        val actualPage = videoGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItemsCount, items.size)
            assertEquals(expectedVideo, items[0].title)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "title, asc, 0, 10, 4, 4, 21.1 Implementação dos testes integrados do findAll",
        "title, desc, 0, 10, 4, 4, System Design no Mercado Livre na prática",
        "createdAt, asc, 0, 10, 4, 4, System Design no Mercado Livre na prática",
        "createdAt, desc, 0, 10, 4, 4, Aula de empreendedorismo",
    )
    fun givenAValidSortAndDirection_whenCallsFindAll_shouldReturnOrdered(
        expectedSort: String,
        expectedDirection: String,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedVideo: String
    ) {
        // given
        mockVideos()

        val expectedTerms = ""

        val aQuery = VideoSearchQuery(
            page = expectedPage,
            perPage = expectedPerPage,
            terms = expectedTerms,
            sort = expectedSort,
            direction = expectedDirection,
            castMembers = emptySet(),
            categories = emptySet(),
            genres = emptySet()
        )

        // when
        val actualPage = videoGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItemsCount, items.size)
            assertEquals(expectedVideo, items[0].title)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "0, 2, 2, 4, 21.1 Implementação dos testes integrados do findAll;Aula de empreendedorismo",
        "1, 2, 2, 4, Não cometa esses erros ao trabalhar com Microsserviços;System Design no Mercado Livre na prática",
    )
    fun givenAValidPaging_whenCallsFindAll_shouldReturnPaged(
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedVideos: String
    ) {
        mockVideos()

        val expectedTerms = ""
        val expectedSort = "title"
        val expectedDirection = "asc"

        val aQuery = VideoSearchQuery(
            page = expectedPage,
            perPage = expectedPerPage,
            terms = expectedTerms,
            sort = expectedSort,
            direction = expectedDirection,
            castMembers = emptySet(),
            categories = emptySet(),
            genres = emptySet()
        )

        // when
        val actualPage = videoGateway.findAll(aQuery)

        // then
        with(actualPage) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItemsCount, items.size)

            for ((index, expectedVideoTitle) in expectedVideos.split(";").withIndex()) {
                val actualVideoTitle = items[index].title
                assertEquals(expectedVideoTitle, actualVideoTitle)
            }
        }
    }

    private fun mockVideos() {
        videoGateway.create(
            Video.newVideo(
                aTitle = "System Design no Mercado Livre na prática",
                aDescription = Fixture.Videos.description(),
                aLaunchYear = Year.of(Fixture.year()),
                aDuration = Fixture.duration(),
                wasOpened = Fixture.bool(),
                wasPublished = Fixture.bool(),
                aRating = Fixture.Videos.rating(),
                categories = setOf(filmes.id),
                genres = setOf(acao.id),
                members = setOf(zoro.id, theRock.id)
            )
        )

        videoGateway.create(
            Video.newVideo(
                aTitle = "Não cometa esses erros ao trabalhar com Microsserviços",
                aDescription = Fixture.Videos.description(),
                aLaunchYear = Year.of(Fixture.year()),
                aDuration = Fixture.duration(),
                wasOpened = Fixture.bool(),
                wasPublished = Fixture.bool(),
                aRating = Fixture.Videos.rating()
            )
        )

        videoGateway.create(
            Video.newVideo(
                aTitle = "21.1 Implementação dos testes integrados do findAll",
                aDescription = Fixture.Videos.description(),
                aLaunchYear = Year.of(Fixture.year()),
                aDuration = Fixture.duration(),
                wasOpened = Fixture.bool(),
                wasPublished = Fixture.bool(),
                aRating = Fixture.Videos.rating(),
                categories = setOf(animes.id),
                genres = setOf(acao.id),
                members = setOf(theRock.id)
            )
        )

        videoGateway.create(
            Video.newVideo(
                aTitle = "Aula de empreendedorismo",
                aDescription = Fixture.Videos.description(),
                aLaunchYear = Year.of(Fixture.year()),
                aDuration = Fixture.duration(),
                wasOpened = Fixture.bool(),
                wasPublished = Fixture.bool(),
                aRating = Fixture.Videos.rating(),
                categories = setOf(animes.id),
                genres = setOf(shonen.id),
                members = setOf(zoro.id)
            )
        )
    }
}
