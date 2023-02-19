package com.lukinhasssss.admin.catalogo.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.lukinhasssss.admin.catalogo.ControllerTest
import com.lukinhasssss.admin.catalogo.application.video.create.CreateVideoOutput
import com.lukinhasssss.admin.catalogo.application.video.create.CreateVideoUseCase
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import com.lukinhasssss.admin.catalogo.infrastructure.video.models.CreateVideoRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post
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
}
