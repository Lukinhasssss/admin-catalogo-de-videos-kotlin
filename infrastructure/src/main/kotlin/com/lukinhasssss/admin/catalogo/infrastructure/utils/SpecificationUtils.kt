package com.lukinhasssss.admin.catalogo.infrastructure.utils

import com.lukinhasssss.admin.catalogo.infrastructure.utils.SqlUtils.like
import org.springframework.data.jpa.domain.Specification
import java.util.Locale

object SpecificationUtils {

    /**
     * @param prop É a propriedade que queremos filtrar
     * @param term É o conteúdo que queremos filtrar
     */
    fun <T> like(prop: String, term: String): Specification<T> {
        return Specification<T> { root, _, criteriaBuilder ->
            criteriaBuilder.like(
                criteriaBuilder.upper(root.get(prop)),
                like(term.uppercase(Locale.getDefault()))
            )
        }
    }
}
