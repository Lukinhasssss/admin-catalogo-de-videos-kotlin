package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.lukinhasssss.admin.catalogo.JacksonTest
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import kotlin.test.Test

@JacksonTest
class VideoEncoderResultTest {

    @Autowired
    private lateinit var json: JacksonTester<VideoEncoderResult>

    @Test
    fun testUnmarshallSuccessResult() {
        // given
        val expectedId = IdUtils.uuid()
        val expectedOutputBucket = "lukinhastest"
        val expectedStatus = "COMPLETED"
        val expectedEncoderVideoFolder = "anyfolder"
        val expectedResourceId = IdUtils.uuid()
        val expectedFilePath = "any.mp4"
        val expectedMetadata = VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath)

        val json = """
          {
            "status": "$expectedStatus",
            "id": "$expectedId",
            "output_bucket_path": "$expectedOutputBucket",
            "video": {
              "encoded_video_folder": "$expectedEncoderVideoFolder",
              "resource_id": "$expectedResourceId",
              "file_path": "$expectedFilePath"
            }
          }
        """

        // when
        val actualResult = this.json.parse(json)

        // then
        assertThat(actualResult)
            .isInstanceOf(VideoEncoderCompleted::class.java)
            .hasFieldOrPropertyWithValue("id", expectedId)
            .hasFieldOrPropertyWithValue("outputBucket", expectedOutputBucket)
            .hasFieldOrPropertyWithValue("video", expectedMetadata)
    }

    @Test
    fun testMarshallSuccessResult() {
        // given
        val expectedId = IdUtils.uuid()
        val expectedOutputBucket = "codeeducationtest"
        val expectedStatus = "COMPLETED"
        val expectedEncoderVideoFolder = "anyfolder"
        val expectedResourceId = IdUtils.uuid()
        val expectedFilePath = "any.mp4"
        val expectedMetadata = VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath)

        val aResult = VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata)

        // when
        val actualResult = this.json.write(aResult)

        // then
        assertThat(actualResult)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.output_bucket_path", expectedOutputBucket)
            .hasJsonPathValue("$.status", expectedStatus)
            .hasJsonPathValue("$.video.encoded_video_folder", expectedEncoderVideoFolder)
            .hasJsonPathValue("$.video.resource_id", expectedResourceId)
            .hasJsonPathValue("$.video.file_path", expectedFilePath)
    }

    @Test
    fun testUnmarshallErrorResult() {
        // given
        val expectedMessage = "Resource not found"
        val expectedStatus = "ERROR"
        val expectedResourceId = IdUtils.uuid()
        val expectedFilePath = "any.mp4"
        val expectedVideoMessage =
            VideoMessage(expectedResourceId, expectedFilePath)

        val json = """
          {
            "status": "$expectedStatus",
            "error": "$expectedMessage",
            "message": {
              "resource_id": "$expectedResourceId",
              "file_path": "$expectedFilePath"
            }
          }
        """

        // when
        val actualResult = this.json.parse(json)

        // then
        assertThat(actualResult)
            .isInstanceOf(VideoEncoderError::class.java)
            .hasFieldOrPropertyWithValue("error", expectedMessage)
            .hasFieldOrPropertyWithValue("message", expectedVideoMessage)
    }

    @Test
    fun testMarshallErrorResult() {
        // given
        val expectedMessage = "Resource not found"
        val expectedStatus = "ERROR"
        val expectedResourceId = IdUtils.uuid()
        val expectedFilePath = "any.mp4"
        val expectedVideoMessage =
            VideoMessage(expectedResourceId, expectedFilePath)

        val aResult = VideoEncoderError(expectedVideoMessage, expectedMessage)

        // when
        val actualResult = this.json.write(aResult)

        // then
        assertThat(actualResult)
            .hasJsonPathValue("$.status", expectedStatus)
            .hasJsonPathValue("$.error", expectedMessage)
            .hasJsonPathValue("$.message.resource_id", expectedResourceId)
            .hasJsonPathValue("$.message.file_path", expectedFilePath)
    }
}
