package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.ValueObject
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.COMPLETED
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.PENDING
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus.PROCESSING

data class AudioVideoMedia(
    val id: String,
    val checksum: String,
    val name: String,
    val rawLocation: String,
    val encodedLocation: String,
    val status: MediaStatus
) : ValueObject() {

    companion object {
        fun with(
            id: String = IdUtils.uuid(),
            checksum: String,
            name: String,
            rawLocation: String,
            encodedLocation: String = "",
            status: MediaStatus = PENDING
        ) = AudioVideoMedia(id, checksum, name, rawLocation, encodedLocation, status)
    }

    fun processing() = with(
        id = id,
        checksum = checksum,
        name = name,
        rawLocation = rawLocation,
        encodedLocation = encodedLocation,
        status = PROCESSING
    )

    fun completed(encodedPath: String) = with(
        id = id,
        checksum = checksum,
        name = name,
        rawLocation = rawLocation,
        encodedLocation = encodedPath,
        status = COMPLETED
    )

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
