package com.lukinhasssss.admin.catalogo.infrastructure.video.persistence

import com.lukinhasssss.admin.catalogo.domain.video.Rating
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class RatingConverter : AttributeConverter<Rating, String> {

    override fun convertToDatabaseColumn(attribute: Rating): String =
        attribute.description

    override fun convertToEntityAttribute(dbData: String): Rating =
        Rating.of(dbData)
}
