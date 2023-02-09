package com.lukinhasssss.admin.catalogo.application.video.media.get

import com.lukinhasssss.admin.catalogo.domain.resource.Resource

data class MediaOutput(
    val content: ByteArray,
    val contentType: String,
    val name: String
) {

    companion object {
        fun with(aResource: Resource): MediaOutput = with(aResource) {
            MediaOutput(content, contentType, name)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaOutput

        if (!content.contentEquals(other.content)) return false
        if (contentType != other.contentType) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content.contentHashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
