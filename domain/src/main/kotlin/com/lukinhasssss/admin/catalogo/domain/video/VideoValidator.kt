package com.lukinhasssss.admin.catalogo.domain.video

import com.lukinhasssss.admin.catalogo.domain.validation.Error
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import com.lukinhasssss.admin.catalogo.domain.validation.Validator

class VideoValidator(
    validationHandler: ValidationHandler,
    private val aVideo: Video
) : Validator(validationHandler) {

    override fun validate() {
        checkTitleConstraints()
        checkDescriptionConstraints()
    }

    companion object {
        const val TITLE_MAX_LENGTH = 255
        const val DESCRIPTION_MAX_LENGTH = 4_000
    }

    private fun checkTitleConstraints() {
        with(aVideo.title) {
            if (isNullOrBlank()) {
                validationHandler.append(Error(message = "'title' should not be empty"))
                return
            }

            val titleLength = trim().length

            if (titleLength > TITLE_MAX_LENGTH)
                validationHandler.append(Error(message = "'title' must be between 1 and 255 characters"))
        }
    }

    private fun checkDescriptionConstraints() {
        with(aVideo.description) {
            if (isEmpty()) {
                validationHandler.append(Error(message = "'description' should not be empty"))
                return
            }

            val descriptionLength = trim().length

            if (descriptionLength > DESCRIPTION_MAX_LENGTH)
                validationHandler.append(Error(message = "'description' must be between 1 and 4000 characters"))
        }
    }
}
