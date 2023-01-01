package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.ValueObject

data class Resource(
    val content: ByteArray,
    val contentType: String,
    val name: String,
    val type: Type
) : ValueObject() {

    companion object {
        fun with(content: ByteArray, contentType: String, name: String, type: Type) =
            Resource(content, contentType, name, type)
    }

    enum class Type {
        VIDEO,
        TRAILER,
        BANNER,
        THUMBNAIL,
        THUMBNAIL_HALF
    }
}
