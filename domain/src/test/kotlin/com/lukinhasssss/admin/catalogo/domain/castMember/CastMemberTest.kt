package com.lukinhasssss.admin.catalogo.domain.castMember

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CastMemberTest {

    @Test
    fun givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
        val expectedName = "Vin Diesel"
        val expectedType = CastMemberType.ACTOR

        val actualMember = CastMember.newMember(expectedName, expectedType)

        with(actualMember) {
            assertNotNull(this)
            assertNotNull(id)
            assertEquals(expectedName, name)
            assertEquals(expectedType, type)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertEquals(createdAt, updatedAt)
        }
    }
}
