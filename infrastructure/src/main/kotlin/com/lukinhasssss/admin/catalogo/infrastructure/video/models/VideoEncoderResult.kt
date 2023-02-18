package com.lukinhasssss.admin.catalogo.infrastructure.video.models

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXISTING_PROPERTY
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

@JsonTypeInfo(use = NAME, include = EXISTING_PROPERTY, property = "status")
@VideoResponseTypes
sealed interface VideoEncoderResult {
    fun getStatus(): String
}
