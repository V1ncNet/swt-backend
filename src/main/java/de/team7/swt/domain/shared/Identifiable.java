package de.team7.swt.domain.shared;

import org.springframework.data.domain.Persistable;

import java.util.Objects;

/**
 * Simple extension of {@link Persistable} which assumes the implementing entity {@link #isNew()} in case
 * {@link #getId()} is not set (yet).
 *
 * @author Vincent Nadoll
 */
@FunctionalInterface
public interface Identifiable<ID extends Identifier> extends Persistable<ID> {

    @Override
    default boolean isNew() {
        return Objects.isNull(getId());
    }
}
