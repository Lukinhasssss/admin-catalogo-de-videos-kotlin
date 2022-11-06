package com.lukinhasssss.admin.catalogo.application.category.create

data class CreateCategoryCommand(
    val name: String,
    val description: String?,
    val isActive: Boolean
) {

    companion object {
        fun with(
            aName: String,
            aDescription: String?,
            isActive: Boolean
        ) = CreateCategoryCommand(aName, aDescription, isActive)
    }
}
