package com.lukinhasssss.admin.catalogo.application.video.retrieve.list

import com.lukinhasssss.admin.catalogo.application.Fixture
import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.pagination.Pagination
import com.lukinhasssss.admin.catalogo.domain.video.Video
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoSearchQuery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ListVideoUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultListVideosUseCase

    @MockK
    private lateinit var videoGateway: VideoGateway

    @Test
    fun givenAValidQuery_whenCallsListVideos_shouldReturnVideos() {
        // given
        val videos = listOf(Fixture.video(), Fixture.video())

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = videos.size.toLong()
        val expectedItems = videos.map { VideoListOutput.from(it) }

        val aQuery = VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val expectedPagination = Pagination(expectedPage, expectedPerPage, expectedTotal, videos)

        every { videoGateway.findAll(aQuery) } returns expectedPagination

        // when
        val actualResult = useCase.execute(aQuery)

        // then
        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItems, items)
        }

        verify(exactly = 1) { videoGateway.findAll(aQuery) }
    }

    @Test
    fun givenAValidQuery_whenCallsListVideoAndResultIsEmpty_shouldReturnVideos() {
        // given
        val videos = listOf<Video>()

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = 0L
        val expectedItems = listOf<VideoListOutput>()

        val aQuery = VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val expectedPagination = Pagination(expectedPage, expectedPerPage, expectedTotal, videos)

        every { videoGateway.findAll(aQuery) } returns expectedPagination

        // when
        val actualResult = useCase.execute(aQuery)

        // then
        with(actualResult) {
            assertEquals(expectedPage, currentPage)
            assertEquals(expectedPerPage, perPage)
            assertEquals(expectedTotal, total)
            assertEquals(expectedItems, items)
        }

        verify(exactly = 1) { videoGateway.findAll(aQuery) }
    }

    @Test
    fun givenAValidQuery_whenGatewayThrowsException_thenReturnException() {
        // given
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedErrorMessage = "Gateway error"

        val aQuery = VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        every { videoGateway.findAll(aQuery) } throws IllegalStateException(expectedErrorMessage)

        // when
        val actualException = assertThrows<IllegalStateException> { useCase.execute(aQuery) }

        // then
        assertEquals(expectedErrorMessage, actualException.message)

        verify(exactly = 1) { videoGateway.findAll(aQuery) }
    }
}
