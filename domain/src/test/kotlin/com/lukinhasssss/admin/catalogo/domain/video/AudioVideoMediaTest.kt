package com.lukinhasssss.admin.catalogo.domain.video

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame

class AudioVideoMediaTest {

    @Test
    fun givenValidParams_whenCallsNewAudioVideo_shouldReturnInstance() {
        // given
        val expectedChecksum = "abc"
        val expectedName = "Banner.png"
        val expectedRawLocation = "/images/ac"
        val expectedEncodedLocation = "/images/ac-encoded"
        val expectedStatus = MediaStatus.PENDING

        // when
        val actualVideo = AudioVideoMedia.with(expectedChecksum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedStatus)

        // then
        with(actualVideo) {
            assertNotNull(this)
            assertEquals(expectedChecksum, checksum)
            assertEquals(expectedName, name)
            assertEquals(expectedRawLocation, rawLocation)
            assertEquals(expectedEncodedLocation, encodedLocation)
            assertEquals(expectedStatus, status)
        }
    }

    @Test
    fun givenTwoAudioVideoWithSameChecksumAndLocation_whenCallsEquals_shouldReturnTrue() {
        // given
        val expectedChecksum = "abc"
        val expectedRawLocation = "/images/ac"

        val img1 = AudioVideoMedia.with(expectedChecksum, "Banner_01.png", expectedRawLocation, "", MediaStatus.PENDING)
        val img2 = AudioVideoMedia.with(expectedChecksum, "Banner_02.png", expectedRawLocation, "", MediaStatus.PENDING)

        // then
        assertEquals(img1, img2)
        assertNotSame(img1, img2)
    }
}
