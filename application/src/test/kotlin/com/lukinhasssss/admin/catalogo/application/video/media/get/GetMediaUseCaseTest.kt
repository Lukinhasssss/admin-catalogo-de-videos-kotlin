package com.lukinhasssss.admin.catalogo.application.video.media.get

import com.lukinhasssss.admin.catalogo.application.UseCaseTest
import com.lukinhasssss.admin.catalogo.domain.Fixture
import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.video.MediaResourceGateway
import com.lukinhasssss.admin.catalogo.domain.video.VideoID
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class GetMediaUseCaseTest : UseCaseTest() {

    @InjectMockKs
    private lateinit var useCase: DefaultGetMediaUseCase

    @MockK
    private lateinit var mediaResourceGateway: MediaResourceGateway

    @Test
    fun givenVideoIdAndType_whenIsAValidCommand_shouldReturnResource() {
        // given
        val expectedId = VideoID.unique()
        val expectedType = Fixture.Videos.mediaType()
        val expectedResource = Fixture.Videos.resource(expectedType)

        every { mediaResourceGateway.getResource(any(), any()) } returns expectedResource

        val aCommand = GetMediaCommand.with(expectedId.value, expectedType.name)

        // when
        val actualResult = useCase.execute(aCommand)

        // then
        with(actualResult) {
            assertEquals(expectedResource.content, content)
            assertEquals(expectedResource.contentType, contentType)
            assertEquals(expectedResource.name, name)
        }
    }

    @Test
    fun givenVideoIdAndType_whenIsNotFound_shouldReturnNotFoundException() {
        // given
        val expectedId = VideoID.unique()
        val expectedType = Fixture.Videos.mediaType()

        every { mediaResourceGateway.getResource(any(), any()) } returns null

        val aCommand = GetMediaCommand.with(expectedId.value, expectedType.name)

        // when
        assertThrows<NotFoundException> { useCase.execute(aCommand) }
    }

    @Test
    fun givenVideoIdAndType_whenTypeDoesntExists_shouldReturnNotFoundException() {
        // given
        val expectedId = VideoID.unique()
        val expectedErrorMessage = "Media type INVALID doesn't exists"

        val aCommand = GetMediaCommand.with(expectedId.value, "INVALID")

        // when
        val actualException = assertThrows<NotFoundException> { useCase.execute(aCommand) }

        // then
        assertEquals(expectedErrorMessage, actualException.message)
    }
}
