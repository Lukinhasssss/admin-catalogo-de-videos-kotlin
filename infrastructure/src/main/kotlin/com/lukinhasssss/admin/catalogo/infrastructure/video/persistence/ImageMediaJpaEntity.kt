package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.video.ImageMedia
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity(name = "ImageMedia")
@Table(name = "videos_image_media")
data class ImageMediaJpaEntity(

    @Id
    val id: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "file_path", nullable = false)
    val filePath: String
) {

    companion object {
        fun from(media: ImageMedia?) = with(media) {
            if (this != null)
                ImageMediaJpaEntity(
                    id = checksum,
                    name = name,
                    filePath = location
                )
            else null
        }
    }

    fun toDomain() = ImageMedia.with(
        checksum = id,
        name = name,
        location = filePath
    )
}
