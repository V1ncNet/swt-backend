package de.team7.swt.domain.shared;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;

/**
 * A {@link UUID} wrapping value object which proxies all defined methods to its encapsulated instance. The VO mainly
 * exist to enforce strictly typed identifiers in JPA and domain entities reducing the misuse of ID based query
 * functions by providing less options.
 *
 * @author Vincent Nadoll
 * @implNote <p>Make sure to annotate the JPA entity's ID field with {@link javax.persistence.EmbeddedId} instead of
 *     {@link javax.persistence.Id}.</p>
 *     <p>The JPA entity's ID accessor must be annotated with
 *     {@literal @GeneratedValue(generator = "<some descriptive name>")}</p>
 *     <p>The JPA entity's ID accessor must be annotated with
 *     {@literal @GenericGenerator(name = "<same as the GeneratedValue-generator value>", strategy = "dyob-id")}</p>
 *     <p>Besides the default constructor an one-arg constructor must be provided which takes an {@link UUID} argument
 *     to ensure Hibernate can generate new unique IDs.</p>
 * @see de.team7.swt.domain.infrastructure.identifier.IdentifierGenerator
 * @see javax.persistence.GeneratedValue
 * @see org.hibernate.annotations.GenericGenerator
 */
@MappedSuperclass
@Access(AccessType.FIELD)
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
