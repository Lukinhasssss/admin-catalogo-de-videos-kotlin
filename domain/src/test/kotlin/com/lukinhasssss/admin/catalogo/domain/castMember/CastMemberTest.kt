package com.lukinhasssss.admin.catalogo.domain.castMember

import com.lukinhasssss.admin.catalogo.domain.UnitTest
import com.lukinhasssss.admin.catalogo.domain.exception.NotificationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CastMemberTest : UnitTest() {

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

    @Test
    fun givenAValidCastMember_whenCallsUpdate_shouldReceiveUpdated() {
        val expectedName = "Vin Diesel"
        val expectedType = CastMemberType.ACTOR

        val aMember = CastMember.newMember("Vin Etanol", CastMemberType.DIRECTOR)

        val expectedId = aMember.id

        assertNotNull(aMember)

        val actualMember = aMember.update(expectedName, expectedType)

        with(actualMember) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedType, type)
            assertEquals(expectedType, type)
            assertEquals(aMember.createdAt, createdAt)
            assertTrue(aMember.updatedAt.isBefore(updatedAt))
        }
    }

    @Test
    fun givenAValidCastMember_whenCallsUpdateWithEmptyName_shouldReceiveANotification() {
        val expectedName = "   "
        val expectedType = CastMemberType.ACTOR
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be empty"

        val aMember = CastMember.newMember("Vin Etanol", CastMemberType.DIRECTOR)

        assertNotNull(aMember)

        val actualException = assertThrows<NotificationException> {
            aMember.update(expectedName, expectedType)
        }

        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

    @Test
    fun givenAValidCastMember_whenCallsUpdateWithNameLengthLessThan3_shouldReceiveANotification() {
        val expectedName = "ab       "
        val expectedType = CastMemberType.ACTOR
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"

        val aMember = CastMember.newMember("Vin Etanol", CastMemberType.DIRECTOR)

        assertNotNull(aMember)

        val actualException = assertThrows<NotificationException> {
            aMember.update(expectedName, expectedType)
        }

        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

    @Test
    fun givenAValidCastMember_whenCallsUpdateWithNameMoreThan255_shouldReceiveANotification() {
        val expectedName = "a".repeat(256)
        val expectedType = CastMemberType.ACTOR
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"

        val aMember = CastMember.newMember("Vin Etanol", CastMemberType.DIRECTOR)

        assertNotNull(aMember)

        val actualException = assertThrows<NotificationException> {
            aMember.update(expectedName, expectedType)
        }

        assertNotNull(actualException)
        assertEquals(expectedErrorCount, actualException.errors.size)
        assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }
}
