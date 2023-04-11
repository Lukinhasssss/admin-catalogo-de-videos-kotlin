package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.ValueObject
import com.lukinhasssss.admin.catalogo.domain.resource.Resource

data class VideoResource(
    val resource: Resource,
    val type: VideoMediaType
) : ValueObject() {

    companion object {
        fun with(aResource: Resource, aType: VideoMediaType) =
            VideoResource(aResource, aType)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VideoResource

        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }
}
