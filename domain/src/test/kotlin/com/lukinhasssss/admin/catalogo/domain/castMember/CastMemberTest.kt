package com.lukinhasssss.admin.catalogo.domain.castMember

import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

    @Test
    fun givenAnInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
        val expectedName = "   "
        val expectedType = CastMemberType.ACTOR
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be empty"

        val actualException = assertThrows<NotificationException> {
            CastMember.newMember(expectedName, expectedType)
        }

        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

    @Test
    fun givenAnInvalidNameWithLengthLessThan3_whenCallsNewMember_shouldReceiveANotification() {
        val expectedName = "ab       "
        val expectedType = CastMemberType.ACTOR
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"

        val actualException = assertThrows<NotificationException> {
            CastMember.newMember(expectedName, expectedType)
        }

        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

    @Test
    fun givenAnInvalidNameWithLengthMoreThan255_whenCallsNewMember_shouldReceiveANotification() {
        val expectedName = "a".repeat(256)
        val expectedType = CastMemberType.ACTOR
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"

        val actualException = assertThrows<NotificationException> {
            CastMember.newMember(expectedName, expectedType)
        }

        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }
}
