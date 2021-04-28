package de.team7.swt.domain.shared;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A {@link UUID} wrapping value object which proxies all defined methods to its encapsulated instance. The VO mainly
 * exist to enforce strictly typed identifiers in JPA and domain entities reducing the misuse of ID based query
 * functions by providing less options.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public abstract class Identifier implements Comparable<Identifier>, Serializable {

    @NonNull
    private final UUID id;

    @Override
    public int compareTo(Identifier that) {
        return Objects.compare(this.id, that.id, UUID::compareTo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Identifier)) {
            return false;
        } else {
            Identifier that = (Identifier) o;
            return Objects.equals(this.id, that.id);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return Objects.toString(id);
    }
}
