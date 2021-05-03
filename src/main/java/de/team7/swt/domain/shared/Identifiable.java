package de.team7.swt.domain.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;

import java.util.Objects;
import javax.persistence.Transient;

/**
 * Simple extension of {@link Persistable} which assumes the implementing entity {@link #isNew()} in case
 * {@link #getId()} is not set (yet).
 *
 * @author Vincent Nadoll
 * @see org.springframework.data.jpa.repository.support.JpaPersistableEntityInformation
 */
@FunctionalInterface
public interface Identifiable<ID extends Identifier> extends Persistable<ID> {

    /**
     * {@inheritDoc}
     * Must be {@link Transient} in order to ensure that no JPA provider complains because of a missing setter.
     *
     * @see <a href="https://jira.spring.io/browse/DATAJPA-622">Hibernate thinks AbstractPersistable.isNew() is a
     * property</a>
     */
    @Transient
    @JsonIgnore
    @Override
    default boolean isNew() {
        return Objects.isNull(getId());
    }
}
