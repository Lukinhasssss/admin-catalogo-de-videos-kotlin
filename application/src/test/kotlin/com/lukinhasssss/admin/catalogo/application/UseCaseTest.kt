package com.lukinhasssss.admin.catalogo.application

import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

@ExtendWith(MockKExtension::class)
abstract class UseCaseTest : BeforeEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        clearAllMocks()
    }

    /*
    override fun beforeEach(context: ExtensionContext?) {
        clearMocks(getMocks())
    }

    abstract fun getMocks(): List<Any>
    */
}
