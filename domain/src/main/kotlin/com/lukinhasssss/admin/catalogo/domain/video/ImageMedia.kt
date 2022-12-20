package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.ValueObject

data class ImageMedia(
    val checksum: String,
    val name: String,
    val location: String
) : ValueObject() {

    companion object {
        fun with(checksum: String, name: String, location: String) =
            ImageMedia(checksum, name, location)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageMedia

        if (checksum != other.checksum) return false
        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = checksum.hashCode()
        result = 31 * result + location.hashCode()
        return result
    }
}
