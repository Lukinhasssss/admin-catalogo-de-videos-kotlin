package com.lukinhasssss.admin.catalogo.application.genre.update

data class UpdateGenreCommand(
    val id: String,
    val name: String,
    val isActive: Boolean,
    val categories: List<String>
) {

    companion object {
        fun with(
            anId: String,
            aName: String,
            isActive: Boolean,
            categories: List<String>
        ) = UpdateGenreCommand(id = anId, name = aName, isActive = isActive, categories = categories)
    }
}
