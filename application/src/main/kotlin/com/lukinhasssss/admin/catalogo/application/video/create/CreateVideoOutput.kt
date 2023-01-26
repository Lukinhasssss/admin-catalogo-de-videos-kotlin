package com.lukinhasssss.admin.catalogo.application.video.create

import com.lukinhasssss.admin.catalogo.domain.video.Video

data class CreateVideoOutput(val id: String) {
    companion object {
        fun from(aVideo: Video) = CreateVideoOutput(aVideo.id.value)
    }
}
