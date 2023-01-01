package com.lukinhasssss.admin.catalogo.domain.video

enum class Rating(name: String) {
    ER(name = "ER"),
    L(name = "L"),
    AGE_10(name = "10"),
    AGE_12(name = "12"),
    AGE_14(name = "14"),
    AGE_16(name = "16"),
    AGE_18(name = "18");

    companion object {
        fun of(label: String) = values().firstOrNull {
            it.name.equals(label, ignoreCase = true)
        }
    }
}
