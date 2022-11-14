package com.lukinhasssss.admin.catalogo.domain.exception

open class NoStacktraceException(
    message: String?
) : RuntimeException(message, null, true, false)
