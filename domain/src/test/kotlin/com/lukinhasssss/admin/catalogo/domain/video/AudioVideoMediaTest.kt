package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.UnitTest
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.PENDING
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame

class AudioVideoMediaTest : UnitTest() {

    @Test
    fun givenValidParams_whenCallsNewAudioVideo_shouldReturnInstance() {
        // given
        val expectedChecksum = "abc"
        val expectedName = "Banner.png"
        val expectedRawLocation = "/images/ac"
        val expectedEncodedLocation = "/images/ac-encoded"
        val expectedStatus = PENDING

        // when
        val actualVideo = AudioVideoMedia.with(
            checksum = expectedChecksum,
            name = expectedName,
            rawLocation = expectedRawLocation,
            encodedLocation = expectedEncodedLocation,
            status = expectedStatus
        )

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

        val img1 = AudioVideoMedia.with(
            checksum = expectedChecksum,
            name = "Banner_01.png",
            rawLocation = expectedRawLocation,
            encodedLocation = "",
            status = PENDING
        )
        val img2 = AudioVideoMedia.with(
            checksum = expectedChecksum,
            name = "Banner_02.png",
            rawLocation = expectedRawLocation,
            encodedLocation = "",
            status = PENDING
        )

        // then
        assertEquals(img1, img2)
        assertNotSame(img1, img2)
    }
}
