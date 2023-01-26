package com.lukinhasssss.admin.catalogo.domain.exception

data class InternalErrorException(
    override val message: String,
    val throwable: Throwable
) : NoStacktraceException(message, throwable) {

    companion object {
        fun with(message: String, throwable: Throwable): InternalErrorException {
            return InternalErrorException(message = message, throwable = throwable)
        }
    }
}
