package com.lukinhasssss.admin.catalogo.application.video.delete

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.exception.InternalErrorException
import com.lukinhasssss.admin.catalogo.domain.video.VideoGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class DeleteVideoUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultDeleteVideoUseCase

    @MockK
    private lateinit var videoGateway: VideoGateway

    @Test
    fun givenAValidId_whenCallsDeleteVideo_shouldDeleteIt() {
        // given
        val expectedId = VideoID.unique()

        every { videoGateway.deleteById(expectedId) } returns Unit

        // when
        assertDoesNotThrow { useCase.execute(expectedId.value) }

        // then
        verify { videoGateway.deleteById(expectedId) }
    }

    @Test
    fun givenAnInvalidId_whenCallsDeleteVideo_shouldBeOk() {
        // given
        val expectedId = VideoID.from("123")

        every { videoGateway.deleteById(expectedId) } returns Unit

        // when
        assertDoesNotThrow { useCase.execute(expectedId.value) }

        // then
        verify { videoGateway.deleteById(expectedId) }
    }

    @Test
    fun givenAValidId_whenCallsDeleteVideoAndGatewayThrowsException_shouldReceiveException() {
        // given
        val expectedId = VideoID.from("123")

        every {
            videoGateway.deleteById(expectedId)
        } throws InternalErrorException("Error on delete video", RuntimeException())

        // when
        assertThrows<InternalErrorException> { useCase.execute(expectedId.value) }

        // then
        verify { videoGateway.deleteById(expectedId) }
    }
}
