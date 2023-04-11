package com.lukinhasssss.admin.catalogo.infrastructure.services.impl

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.lukinhasssss.admin.catalogo.domain.resource.Resource
import com.lukinhasssss.admin.catalogo.infrastructure.services.StorageService
import java.util.stream.StreamSupport

class GoogleCloudStorageService(
    private val bucket: String,
    private val storage: Storage
) : StorageService {

    override fun store(name: String, resource: Resource) {
        val blobInfo = BlobInfo.newBuilder(bucket, name)
            .setContentType(resource.contentType)
            .setCrc32cFromHexString(resource.checksum)
            .build()

        storage.create(blobInfo, resource.content)
    }

    override fun list(prefix: String): List<String> {
        val blobs = storage.list(bucket, Storage.BlobListOption.prefix(prefix))

        return StreamSupport.stream(blobs.iterateAll().spliterator(), false)
            .map { it.blobId }
            .map { it.name }
            .toList()
    }

    override fun deleteAll(names: Collection<String>) {
        val blobs = names.map { BlobId.of(bucket, it) }

        storage.delete(blobs)
    }

    override fun get(name: String): Resource? =
        storage.get(bucket, name).toResource()

    private fun Blob?.toResource() = if (this != null) {
        Resource.with(
            checksum = crc32cToHexString,
            content = getContent(),
            contentType = contentType,
            name = name
        )
    } else {
        null
    }
}
