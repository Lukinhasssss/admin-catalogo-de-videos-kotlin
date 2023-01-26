package com.lukinhasssss.admin.catalogo.domain.video

interface MediaResourceGateway {

    fun storeAudioVideo(anId: VideoID, aResource: Resource): AudioVideoMedia

    fun storeImage(anId: VideoID, aResource: Resource): ImageMedia

    fun clearResources(anId: VideoID)
}
