package de.team7.swt.core.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * An extension to Spring's {@literal CrudRepository} supporting {@literal List}s instead of {@literal Iterable}s.
 *
 * @author Vincent Nadoll
 */
@NoRepositoryBean
public interface ListSupportingCrudRepository<T, ID> extends CrudRepository<T, ID> {

    /**
     * {@inheritDoc}
     */
    @Override
    <S extends T> List<S> saveAll(Iterable<S> entities);

    /**
     * {@inheritDoc}
     */
    @Override
    List<T> findAll();

    /**
     * {@inheritDoc}
     */
    @Override
    List<T> findAllById(Iterable<ID> ids);
}
