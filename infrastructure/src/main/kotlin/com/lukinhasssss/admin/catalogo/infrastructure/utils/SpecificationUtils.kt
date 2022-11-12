package com.lukinhasssss.admin.catalogo.infrastructure.utils

import org.springframework.data.jpa.domain.Specification
import java.util.Locale

class SpecificationUtils {

    companion object {
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

        private fun like(term: String): String {
            return "%$term%"
        }
    }
}
