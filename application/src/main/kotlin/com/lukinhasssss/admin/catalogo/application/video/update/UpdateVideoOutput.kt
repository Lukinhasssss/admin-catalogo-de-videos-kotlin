package com.lukinhasssss.admin.catalogo.application.video.update

import com.lukinhasssss.admin.catalogo.domain.video.Video

data class UpdateVideoOutput(val id: String) {
    companion object {
        fun from(aVideo: Video) = UpdateVideoOutput(aVideo.id.value)
    }
}
