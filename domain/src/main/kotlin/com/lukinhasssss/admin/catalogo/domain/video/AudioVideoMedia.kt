package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.ValueObject

data class AudioVideoMedia(
    val checksum: String,
    val name: String,
    val rawLocation: String,
    val encodedLocation: String,
    val status: MediaStatus
) : ValueObject() {

    companion object {
        fun with(
            checksum: String,
            name: String,
            rawLocation: String,
            encodedLocation: String,
            status: MediaStatus
        ) = AudioVideoMedia(checksum, name, rawLocation, encodedLocation, status)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AudioVideoMedia

        if (checksum != other.checksum) return false
        if (rawLocation != other.rawLocation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = checksum.hashCode()
        result = 31 * result + rawLocation.hashCode()
        return result
    }
}
