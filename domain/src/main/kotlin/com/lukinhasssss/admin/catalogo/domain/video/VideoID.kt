package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.Identifier
import java.util.UUID

data class VideoID(
    override val value: String
) : Identifier() {

    companion object {
        fun from(anId: String) = VideoID(value = anId.lowercase())

        fun from(anId: UUID) = from(anId = anId.toString())

        fun unique() = from(anId = UUID.randomUUID())
    }
}
