package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.resource.Resource

interface MediaResourceGateway {

    fun storeAudioVideo(anId: VideoID, videoResource: VideoResource): AudioVideoMedia

    fun storeImage(anId: VideoID, imageResource: VideoResource): ImageMedia

    fun getResource(anId: VideoID, aType: VideoMediaType): Resource?

    fun clearResources(anId: VideoID)
}
