package com.lukinhasssss.admin.catalogo.application.video.media.get

data class GetMediaCommand(
    val videoId: String,
    val mediaType: String
) {

    companion object {
        fun with(anId: String, aType: String) =
            GetMediaCommand(anId, aType)
    }
}
