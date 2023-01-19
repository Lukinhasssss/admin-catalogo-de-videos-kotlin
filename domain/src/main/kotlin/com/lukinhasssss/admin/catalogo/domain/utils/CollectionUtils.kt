package com.lukinhasssss.admin.catalogo.domain.utils

object CollectionUtils {

    fun <IN, out> mapTo(list: Set<IN>, mapper: (IN) -> out): Set<out> =
        list.stream().map(mapper).toList().toSet()
}
