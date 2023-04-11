package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.ValueObject
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils

data class ImageMedia(
    val id: String,
    val checksum: String,
    val name: String,
    val location: String
) : ValueObject() {

    companion object {
        fun with(id: String = IdUtils.uuid(), checksum: String, name: String, location: String) =
            ImageMedia(id, checksum, name, location)
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
