package com.lukinhasssss.admin.catalogo.infrastructure.utils

import com.google.common.hash.Hashing

object HashingUtils {

    private val CHECKSUM = Hashing.crc32c()

    fun checksum(content: ByteArray) =
        CHECKSUM.hashBytes(content).toString()
}
