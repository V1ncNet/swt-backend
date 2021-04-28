package de.team7.swt.domain.shared;

import lombok.NonNull;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Base class for Aggregate Root entities as they are described in DDD. It exposes a {@link #register(ApplicationEvent)}
 * method allowing subclasses to register events to be published as soon as the aggregate using calls to
 * {@link org.springframework.data.repository.CrudRepository#save(Object)}.
 *
 * @author Vincent Nadoll
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#core.domain-events">Publishing
 * Events from Aggregate Roots</a>
 */
public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    private final transient Collection<ApplicationEvent> events = new ArrayList<>();

    protected final <T extends ApplicationEvent> T register(@NonNull T event) {
        events.add(event);
        return event;
    }

    @DomainEvents
    @SuppressWarnings("unused")
    Collection<ApplicationEvent> getEvents() {
        return Collections.unmodifiableCollection(events);
    }

    @AfterDomainEventPublication
    @SuppressWarnings("unused")
    void wipeEvents() {
        events.clear();
    }
}
