package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.exception.NotFoundException
import com.lukinhasssss.admin.catalogo.domain.validation.Error

enum class VideoMediaType {
    VIDEO,
    TRAILER,
    BANNER,
    THUMBNAIL,
    THUMBNAIL_HALF;

    companion object {
        fun of(value: String): VideoMediaType {
            val videoMediaType = values().firstOrNull {
                it.name.equals(other = value, ignoreCase = true)
            }

            return videoMediaType ?: throw typeNotFound(value)
        }

        private fun typeNotFound(type: String): NotFoundException {
            val error = Error(message = "Media type $type doesn't exists")
            return NotFoundException.with(error)
        }
    }
}
