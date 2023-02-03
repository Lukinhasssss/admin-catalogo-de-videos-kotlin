package com.lukinhasssss.admin.catalogo.domain.resource

import com.lukinhasssss.admin.catalogo.domain.ValueObject

data class Resource(
    val checksum: String,
    val content: ByteArray,
    val contentType: String,
    val name: String
) : ValueObject() {

    companion object {
        fun with(checksum: String, content: ByteArray, contentType: String, name: String) =
            Resource(checksum, content, contentType, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resource

        if (checksum != other.checksum) return false
        if (!content.contentEquals(other.content)) return false
        if (contentType != other.contentType) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = checksum.hashCode()
        result = 31 * result + content.contentHashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
