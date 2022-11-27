package com.lukinhasssss.admin.catalogo.application.genre.create

data class CreateGenreCommand(
    val name: String,
    val isActive: Boolean,
    val categories: List<String>
) {

    companion object {
        fun with(aName: String, isActive: Boolean, categories: List<String>) =
            CreateGenreCommand(aName, isActive, categories)
    }
}
