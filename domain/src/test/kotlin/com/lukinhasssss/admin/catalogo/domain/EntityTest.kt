package com.lukinhasssss.admin.catalogo.domain

import com.lukinhasssss.admin.catalogo.domain.event.DomainEvent
import com.lukinhasssss.admin.catalogo.domain.event.DomainEventPublisher
import com.lukinhasssss.admin.catalogo.domain.utils.IdUtils
import com.lukinhasssss.admin.catalogo.domain.utils.InstantUtils
import com.lukinhasssss.admin.catalogo.domain.validation.ValidationHandler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

class EntityTest {

    @Test
    fun givenEmptyDomainEvents_whenCallsRegisterEvent_shouldAddEventToList() {
        // given
        val expectedEvents = 1
        val anEntity = DummyEntity(DummyID())

        // when
        anEntity.registerEvent(DummyEvent())

        // then
        assertEquals(expectedEvents, anEntity.domainEvents.size)
    }

    @Test
    fun givenAFewDomainEvents_whenCallsPublishEvents_shouldCallPublisherAndClearTheList() {
        // given
        val expectedEvents = 0
        val expectedSentEvents = 2
        val counter = AtomicInteger(0)
        val anEntity = DummyEntity(DummyID(), ArrayList())

        anEntity.registerEvent(DummyEvent())
        anEntity.registerEvent(DummyEvent())

        assertEquals(2, anEntity.domainEvents.size)

        // when
        anEntity.publishDomainEvents(object : DomainEventPublisher {
            override fun publishEvent(event: DomainEvent) {
                counter.incrementAndGet()
            }
        })

        // then
        assertEquals(expectedEvents, anEntity.domainEvents.size)
        assertEquals(expectedSentEvents, counter.get())
    }

    class DummyEvent(
        override val occurredOn: Instant = InstantUtils.now()
    ) : DomainEvent

    class DummyID(
        override val value: String = IdUtils.uuid()
    ) : Identifier()

    class DummyEntity(
        id: DummyID,
        domainEvents: MutableList<DomainEvent> = mutableListOf()
    ) : Entity<DummyID>(id, domainEvents) {
        override fun validate(handler: ValidationHandler) {
            TODO("Not yet implemented")
        }
    }
}
