package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.event.DomainEvent
import com.lukinhasssss.admin.catalogo.domain.utils.InstantUtils
import java.time.Instant

data class VideoMediaCreated(
    val resourceId: String,
    val filePath: String,
    override val occurredOn: Instant = InstantUtils.now()
) : DomainEvent
