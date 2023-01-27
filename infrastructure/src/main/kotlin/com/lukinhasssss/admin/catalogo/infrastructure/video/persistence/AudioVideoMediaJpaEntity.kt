package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.video.AudioVideoMedia
import com.lukinhasssss.admin.catalogo.domain.video.MediaStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity(name = "AudioVideoMedia")
@Table(name = "videos_video_media")
data class AudioVideoMediaJpaEntity(

    @Id
    val id: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "file_path", nullable = false)
    val filePath: String,

    @Column(name = "encoded_path", nullable = false)
    val encodedPath: String,

    @Column(name = "status", nullable = false)
    @Enumerated(value = STRING)
    val status: MediaStatus
) {

    companion object {
        fun from(media: AudioVideoMedia?) = with(media) {
            if (this != null)
                AudioVideoMediaJpaEntity(
                    id = checksum,
                    name = name,
                    filePath = rawLocation,
                    encodedPath = encodedLocation,
                    status = status
                )
            else null
        }
    }

    fun toDomain() = AudioVideoMedia.with(
        checksum = id,
        name = name,
        rawLocation = filePath,
        encodedLocation = encodedPath,
        status = status
    )
}
