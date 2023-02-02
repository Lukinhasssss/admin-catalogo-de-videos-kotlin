package com.lukinhasssss.admin.catalogo.domain.video

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame

class ImageMediaTest {

    @Test
    fun givenValidParams_whenCallsNewImage_shouldReturnInstance() {
        // given
        val expectedChecksum = "abc"
        val expectedName = "Banner.png"
        val expectedLocation = "/images/ac"

        // when
        val actualImage = ImageMedia.with(
            checksum = expectedChecksum,
            name = expectedName,
            location = expectedLocation
        )

        // then
        with(actualImage) {
            assertNotNull(this)
            assertEquals(expectedChecksum, checksum)
            assertEquals(expectedName, name)
            assertEquals(expectedLocation, location)
        }
    }

    @Test
    fun givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_shouldReturnTrue() {
        // given
        val expectedChecksum = "abc"
        val expectedLocation = "/images/ac"

        val img1 = ImageMedia.with(
            checksum = expectedChecksum,
            name = "Banner_01.png",
            location = expectedLocation
        )
        val img2 = ImageMedia.with(
            checksum = expectedChecksum,
            name = "Banner_02.png",
            location = expectedLocation
        )

        // then
        assertEquals(img1, img2)
        assertNotSame(img1, img2)
    }
}
