package com.lukinhasssss.admin.catalogo.domain.video

interface MediaResourceGateway {

    fun storeAudioVideo(anId: VideoID, aResource: VideoResource): AudioVideoMedia

    fun storeImage(anId: VideoID, aResource: VideoResource): ImageMedia

    fun clearResources(anId: VideoID)
}
