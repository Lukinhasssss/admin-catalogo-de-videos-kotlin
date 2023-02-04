package com.lukinhasssss.admin.catalogo.domain.video

interface MediaResourceGateway {

    fun storeAudioVideo(anId: VideoID, videoResource: VideoResource): AudioVideoMedia

    fun storeImage(anId: VideoID, imageResource: VideoResource): ImageMedia

    fun clearResources(anId: VideoID)
}
