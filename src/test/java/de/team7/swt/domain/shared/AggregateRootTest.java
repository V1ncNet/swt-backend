package de.team7.swt.domain.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEvent;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vincent Nadoll
 */
class AggregateRootTest {

    static final UUID ID_VALUE = UUID.fromString("f1ed301e-dcf1-4609-8b50-e1106ffc222f");
    static final Id ID = new Id(ID_VALUE);

    AggregateRoot<Id> aggregateRoot;

    @BeforeEach
    void setUp() {
        aggregateRoot = new AggregateRoot<>() {
            @Override
            public Id getId() {
                return ID;
            }
        };
    }

    @Test
    void newInstance_shouldNotContainEvents() {
        Collection<ApplicationEvent> events = aggregateRoot.getEvents();

        assertNotNull(events);
        assertEquals(0, events.size());
    }

    @Test
    void addToEventCollection_shouldThrowException() {
        assertThrows(UnsupportedOperationException.class, () -> aggregateRoot.getEvents().add(new Event()));
    }

    @Test
    void registerOnceThanGet_shouldReturnCollectionWithEvent() {
        Event event = new Event();

        aggregateRoot.register(event);
        Collection<ApplicationEvent> events = aggregateRoot.getEvents();

        assertNotNull(events);
        assertEquals(1, events.size());
        assertTrue(events.contains(event));
    }

    @Test
    void registerOnceThanWipe_shouldClearEvents() {
        Event event = new Event();

        aggregateRoot.register(event);
        aggregateRoot.wipeEvents();

        assertEquals(0, aggregateRoot.getEvents().size());
    }

    static class Event extends ApplicationEvent {
        public Event() {
            super("");
        }
    }
}
