package com.lukinhasssss.admin.catalogo.application

import com.lukinhasssss.admin.catalogo.domain.Identifier
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

@ExtendWith(MockKExtension::class)
@Tag(value = "unitTest")
abstract class UseCaseTest : BeforeEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        clearAllMocks()
    }

    protected fun Iterable<Identifier>.asString() = map { it.value }
}
