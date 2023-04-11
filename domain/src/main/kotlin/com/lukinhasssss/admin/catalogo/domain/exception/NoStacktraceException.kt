package com.lukinhasssss.admin.catalogo.domain.exception

open class NoStacktraceException(
    message: String?,
    throwable: Throwable? = null
) : RuntimeException(message, throwable, true, false)
