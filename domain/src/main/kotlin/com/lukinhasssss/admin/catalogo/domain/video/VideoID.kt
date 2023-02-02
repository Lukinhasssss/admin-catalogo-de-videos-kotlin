package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.Identifier
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils

data class VideoID(
    override val value: String
) : Identifier() {

    companion object {
        fun unique() = from(anId = IdUtils.uuid())
        fun from(anId: String) = VideoID(value = anId.lowercase())
    }
}
