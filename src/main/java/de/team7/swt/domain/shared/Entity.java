package de.team7.swt.domain.shared;

import java.util.Objects;

/**
 * Base class for entities as the are defined in DDD by Eric Evans. Thus entities are equal if their identifiers are the
 * same. Be cautious when reusing an identifier (with the same value) because the using entities are equal by
 * definition, which might result in unexpected behaviors in data structures which utilize {@link #hashCode()} method.
 *
 * @author Vincent Nadoll
 * @implNote Subclasses must not override {@link #equals(Object)} and {{@link #hashCode()}}
 */
public abstract class Entity<ID extends Identifier> implements Identifiable<ID> {

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Entity)) {
            return false;
        } else {
            Entity<?> that = (Entity<?>) o;
            return Objects.equals(this.getId(), that.getId());
        }
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(getId());
    }
}
