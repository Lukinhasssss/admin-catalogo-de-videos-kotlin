package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.lukinhasssss.admin.catalogo.ControllerTest
import com.lukinhasssss.admin.catalogo.application.video.create.CreateVideoOutput
import com.lukinhasssss.admin.catalogo.application.video.create.CreateVideoUseCase
import com.lukinhasssss.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase
import com.lukinhasssss.admin.catalogo.application.video.retrieve.get.VideoOutput
import com.lukinhasssss.admin.catalogo.application.video.update.UpdateVideoOutput
import com.lukinhasssss.admin.catalogo.application.video.update.UpdateVideoUseCase
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.castMember.CastMemberID
import com.lukinhasssss.admin.catalogo.domain.category.CategoryID
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import com.lukinhasssss.admin.catalogo.domain.genre.GenreID
import com.lukinhasssss.admin.catalogo.domain.utils.CollectionUtils.mapTo
import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.domain.video.VideoMediaType
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.CreateVideoRequest
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.UpdateVideoRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.Year
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ControllerTest(controllers = [VideoAPI::class])
class VideoAPITest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockkBean
    private lateinit var createVideoUseCase: CreateVideoUseCase

    @MockkBean
    private lateinit var getVideoByIdUseCase: GetVideoByIdUseCase

    @MockkBean
    private lateinit var updateVideoUseCase: UpdateVideoUseCase

    @Test
    fun givenAValidCommand_whenCallsCreateFull_shouldReturnAnId() {
        // given
        val anime = Fixture.Categories.animes()
        val shonen = Fixture.Genres.shonen()
        val luffy = Fixture.CastMembers.luffy()

        val expectedId = VideoID.unique()
        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(anime.id.value)
        val expectedGenres = setOf(shonen.id.value)
        val expectedMembers = setOf(luffy.id.value)

        val expectedVideo =
            MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".toByteArray())

        val expectedTrailer =
            MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".toByteArray())

        val expectedBanner =
            MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".toByteArray())

        val expectedThumb =
            MockMultipartFile("thumb_file", "thumbnail.jpg", "image/jpg", "THUMB".toByteArray())

        val expectedThumbHalf =
            MockMultipartFile("thumb_half_file", "thumbnailHalf.jpg", "image/jpg", "THUMBHALF".toByteArray())

        every { createVideoUseCase.execute(any()) } returns CreateVideoOutput(expectedId.value)

        // when
        val aResponse = mvc.multipart(urlTemplate = "/videos") {
            file(expectedVideo)
            file(expectedTrailer)
            file(expectedBanner)
            file(expectedThumb)
            file(expectedThumbHalf)

            param("title", expectedTitle)
            param("description", expectedDescription)
            param("year_launched", expectedLaunchYear.value.toString())
            param("duration", expectedDuration.toString())
            param("opened", expectedOpened.toString())
            param("published", expectedPublished.toString())
            param("rating", expectedRating.description)
            param("cast_members_id", luffy.id.value)
            param("categories_id", anime.id.value)
            param("genres_id", shonen.id.value)

            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.MULTIPART_FORM_DATA
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isCreated() }

            header {
                string(name = "Location", value = "/videos/${expectedId.value}")
                string(name = "Content-Type", value = MediaType.APPLICATION_JSON_VALUE)
            }

            jsonPath("$.id", equalTo(expectedId.value))
        }

        verify {
            createVideoUseCase.execute(
                withArg {
                    assertEquals(expectedTitle, it.title)
                    assertEquals(expectedDescription, it.description)
                    assertEquals(expectedLaunchYear.value, it.launchedAt)
                    assertEquals(expectedDuration, it.duration)
                    assertEquals(expectedOpened, it.opened)
                    assertEquals(expectedPublished, it.published)
                    assertEquals(expectedRating.description, it.rating)
                    assertEquals(expectedCategories, it.categories)
                    assertEquals(expectedGenres, it.genres)
                    assertEquals(expectedMembers, it.members)
                    assertEquals(expectedVideo.originalFilename, it.video?.name)
                    assertEquals(expectedTrailer.originalFilename, it.trailer?.name)
                    assertEquals(expectedBanner.originalFilename, it.banner?.name)
                    assertEquals(expectedThumb.originalFilename, it.thumbnail?.name)
                    assertEquals(expectedThumbHalf.originalFilename, it.thumbnailHalf?.name)
                }
            )
        }
    }

    @Test
    fun givenAValidCommand_whenCallsCreatePartial_shouldReturnAnId() {
        // given
        val anime = Fixture.Categories.animes()
        val shonen = Fixture.Genres.shonen()
        val luffy = Fixture.CastMembers.luffy()

        val expectedId = VideoID.unique()
        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(anime.id.value)
        val expectedGenres = setOf(shonen.id.value)
        val expectedMembers = setOf(luffy.id.value)

        val aRequest = CreateVideoRequest(
            title = expectedTitle,
            description = expectedDescription,
            launchYear = expectedLaunchYear.value,
            duration = expectedDuration,
            opened = expectedOpened,
            published = expectedPublished,
            rating = expectedRating.description,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        )

        every { createVideoUseCase.execute(any()) } returns CreateVideoOutput(expectedId.value)

        // when
        val aResponse = mvc.post(urlTemplate = "/videos") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(aRequest)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isCreated() }

            header {
                string(name = "Location", value = "/videos/${expectedId.value}")
                string(name = "Content-Type", value = MediaType.APPLICATION_JSON_VALUE)
            }

            jsonPath("$.id", equalTo(expectedId.value))
        }

        verify {
            createVideoUseCase.execute(
                withArg {
                    assertEquals(expectedTitle, it.title)
                    assertEquals(expectedDescription, it.description)
                    assertEquals(expectedLaunchYear.value, it.launchedAt)
                    assertEquals(expectedDuration, it.duration)
                    assertEquals(expectedOpened, it.opened)
                    assertEquals(expectedPublished, it.published)
                    assertEquals(expectedRating.description, it.rating)
                    assertEquals(expectedCategories, it.categories)
                    assertEquals(expectedGenres, it.genres)
                    assertEquals(expectedMembers, it.members)
                    assertNull(it.video)
                    assertNull(it.trailer)
                    assertNull(it.banner)
                    assertNull(it.thumbnail)
                    assertNull(it.thumbnailHalf)
                }
            )
        }
    }

    @Test
    fun givenAValidId_whenCallsGetById_shouldReturnVideo() {
        // given
        val anime = Fixture.Categories.animes()
        val shonen = Fixture.Genres.shonen()
        val luffy = Fixture.CastMembers.luffy()

        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(anime.id.value)
        val expectedGenres = setOf(shonen.id.value)
        val expectedMembers = setOf(luffy.id.value)

        val expectedVideo = Fixture.Videos.audioVideo(VideoMediaType.VIDEO)
        val expectedTrailer = Fixture.Videos.audioVideo(VideoMediaType.TRAILER)
        val expectedBanner = Fixture.Videos.imageMedia(VideoMediaType.BANNER)
        val expectedThumb = Fixture.Videos.imageMedia(VideoMediaType.THUMBNAIL)
        val expectedThumbHalf = Fixture.Videos.imageMedia(VideoMediaType.THUMBNAIL_HALF)

        val aVideo = Video.newVideo(
            aTitle = expectedTitle,
            aDescription = expectedDescription,
            aLaunchYear = expectedLaunchYear,
            aDuration = expectedDuration,
            wasOpened = expectedOpened,
            wasPublished = expectedPublished,
            aRating = expectedRating,
            categories = expectedCategories.mapTo { CategoryID.from(it) },
            genres = expectedGenres.mapTo { GenreID.from(it) },
            members = expectedMembers.mapTo { CastMemberID.from(it) }
        )
            .updateVideoMedia(expectedVideo)
            .updateTrailerMedia(expectedTrailer)
            .updateBannerMedia(expectedBanner)
            .updateThumbnailMedia(expectedThumb)
            .updateThumbnailHalfMedia(expectedThumbHalf)

        val expectedId = aVideo.id.value

        every { getVideoByIdUseCase.execute(any()) } returns VideoOutput.from(aVideo)

        // when
        val aResponse = mvc.get(urlTemplate = "/videos/$expectedId") {
            accept = MediaType.APPLICATION_JSON
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isOk() }

            header {
                string(name = "Content-Type", value = MediaType.APPLICATION_JSON_VALUE)
            }

            jsonPath("$.id", equalTo(expectedId))
            jsonPath("$.id", equalTo(expectedId))
            jsonPath("$.title", equalTo(expectedTitle))
            jsonPath("$.description", equalTo(expectedDescription))
            jsonPath("$.year_launched", equalTo(expectedLaunchYear.value))
            jsonPath("$.duration", equalTo(expectedDuration))
            jsonPath("$.opened", equalTo(expectedOpened))
            jsonPath("$.published", equalTo(expectedPublished))
            jsonPath("$.rating", equalTo(expectedRating.description))
            jsonPath("$.created_at", equalTo(aVideo.createdAt.toString()))
            jsonPath("$.updated_at", equalTo(aVideo.updatedAt.toString()))
            jsonPath("$.video.id", equalTo(expectedVideo.id))
            jsonPath("$.video.name", equalTo(expectedVideo.name))
            jsonPath("$.video.checksum", equalTo(expectedVideo.checksum))
            jsonPath("$.video.location", equalTo(expectedVideo.rawLocation))
            // jsonPath("$.video.encoded_location", equalTo(expectedVideo.encodedLocation))
            jsonPath("$.video.status", equalTo(expectedVideo.status.name))
            jsonPath("$.trailer.id", equalTo(expectedTrailer.id))
            jsonPath("$.trailer.name", equalTo(expectedTrailer.name))
            jsonPath("$.trailer.checksum", equalTo(expectedTrailer.checksum))
            jsonPath("$.trailer.location", equalTo(expectedTrailer.rawLocation))
            // jsonPath("$.trailer.encoded_location", equalTo(expectedTrailer.encodedLocation))
            jsonPath("$.trailer.status", equalTo(expectedTrailer.status.name))
            jsonPath("$.banner.id", equalTo(expectedBanner.id))
            jsonPath("$.banner.name", equalTo(expectedBanner.name))
            jsonPath("$.banner.location", equalTo(expectedBanner.location))
            jsonPath("$.banner.checksum", equalTo(expectedBanner.checksum))
            jsonPath("$.thumbnail.id", equalTo(expectedThumb.id))
            jsonPath("$.thumbnail.name", equalTo(expectedThumb.name))
            jsonPath("$.thumbnail.location", equalTo(expectedThumb.location))
            jsonPath("$.thumbnail.checksum", equalTo(expectedThumb.checksum))
            jsonPath("$.thumbnail_half.id", equalTo(expectedThumbHalf.id))
            jsonPath("$.thumbnail_half.name", equalTo(expectedThumbHalf.name))
            jsonPath("$.thumbnail_half.location", equalTo(expectedThumbHalf.location))
            jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbHalf.checksum))
            jsonPath("$.categories_id", equalTo(expectedCategories.toList()))
            jsonPath("$.genres_id", equalTo(expectedGenres.toList()))
            jsonPath("$.cast_members_id", equalTo(expectedMembers.toList()))
        }
    }

    @Test
    fun givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() {
        // given
        val anime = Fixture.Categories.animes()
        val shonen = Fixture.Genres.shonen()
        val luffy = Fixture.CastMembers.luffy()

        val expectedId = VideoID.unique()
        val expectedTitle = Fixture.title()
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(anime.id.value)
        val expectedGenres = setOf(shonen.id.value)
        val expectedMembers = setOf(luffy.id.value)

        val aRequest = UpdateVideoRequest(
            title = expectedTitle,
            description = expectedDescription,
            launchYear = expectedLaunchYear.value,
            duration = expectedDuration,
            opened = expectedOpened,
            published = expectedPublished,
            rating = expectedRating.description,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        )

        every { updateVideoUseCase.execute(any()) } returns UpdateVideoOutput(expectedId.value)

        // when
        val aResponse = mvc.put(urlTemplate = "/videos/${expectedId.value}") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(aRequest)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isOk() }

            header {
                string(name = "Location", value = "/videos/${expectedId.value}")
                string(name = "Content-Type", value = MediaType.APPLICATION_JSON_VALUE)
            }

            jsonPath("$.id", equalTo(expectedId.value))
        }

        verify {
            updateVideoUseCase.execute(
                withArg {
                    assertEquals(expectedTitle, it.title)
                    assertEquals(expectedDescription, it.description)
                    assertEquals(expectedLaunchYear.value, it.launchedAt)
                    assertEquals(expectedDuration, it.duration)
                    assertEquals(expectedOpened, it.opened)
                    assertEquals(expectedPublished, it.published)
                    assertEquals(expectedRating.description, it.rating)
                    assertEquals(expectedCategories, it.categories)
                    assertEquals(expectedGenres, it.genres)
                    assertEquals(expectedMembers, it.members)
                    assertNull(it.video)
                    assertNull(it.trailer)
                    assertNull(it.banner)
                    assertNull(it.thumbnail)
                    assertNull(it.thumbnailHalf)
                }
            )
        }
    }

    @Test
    fun givenAnInvalidCommand_whenCallsUpdateVideo_shouldReturnNotification() {
        // given
        val anime = Fixture.Categories.animes()
        val shonen = Fixture.Genres.shonen()
        val luffy = Fixture.CastMembers.luffy()

        val expectedId = VideoID.unique()
        val expectedTitle = ""
        val expectedDescription = Fixture.Videos.description()
        val expectedLaunchYear = Year.of(Fixture.year())
        val expectedDuration = Fixture.duration()
        val expectedOpened = Fixture.bool()
        val expectedPublished = Fixture.bool()
        val expectedRating = Fixture.Videos.rating()
        val expectedCategories = setOf(anime.id.value)
        val expectedGenres = setOf(shonen.id.value)
        val expectedMembers = setOf(luffy.id.value)

        val expectedErrorCount = 1
        val expectedErrorMessage = "'title' should not be empty"

        val aRequest = UpdateVideoRequest(
            title = expectedTitle,
            description = expectedDescription,
            launchYear = expectedLaunchYear.value,
            duration = expectedDuration,
            opened = expectedOpened,
            published = expectedPublished,
            rating = expectedRating.description,
            categories = expectedCategories,
            genres = expectedGenres,
            members = expectedMembers
        )

        every {
            updateVideoUseCase.execute(any())
        } throws NotificationException(expectedErrorMessage, Notification.create(Error(expectedErrorMessage)))

        // when
        val aResponse = mvc.put(urlTemplate = "/videos/${expectedId.value}") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(aRequest)
        }.andDo { print() }

        // then
        aResponse.andExpect {
            status { isUnprocessableEntity() }

            header {
                string(name = "Content-Type", value = MediaType.APPLICATION_JSON_VALUE)
            }

            jsonPath("$.message", equalTo(expectedErrorMessage))
            jsonPath("$.errors", hasSize<Int>(expectedErrorCount))
            jsonPath("$.errors[0].message", equalTo(expectedErrorMessage))
        }

        verify { updateVideoUseCase.execute(any()) }
    }
}
