package de.team7.data.inmemory.repository.support;

import de.team7.data.domain.Identifier;
import de.team7.data.domain.IdentifierGenerator;
import de.team7.data.inmemory.repository.InMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.util.Assert;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * In-memory generic repository factory which instantiates {@link InMemoryRepository}s via reflection.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class InMemoryRepositoryFactory extends RepositoryFactorySupport {

    @Override
    @SuppressWarnings("unchecked")
    public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return (EntityInformation<T, ID>) InMemoryEntityInformation.getEntityInformation(domainClass);
    }

    @Override
    protected InMemoryRepository<?, ?> getTargetRepository(RepositoryInformation metadata) {
        try {
            Class<?> domainType = metadata.getDomainType();
            DefaultConstructorIdentifierGenerator<Identifier> generator =
                new DefaultConstructorIdentifierGenerator<>(domainType);

            Object repository = getTargetRepositoryViaReflection(metadata, generator);
            Assert.isInstanceOf(InMemoryRepository.class, repository);

            return (InMemoryRepository<?, ?>) repository;
        } catch (NoSuchElementException e) {
            throw new IllegalStateException(
                "No suitable constructor found to instantiate a new identifier generator", e
            );
        }
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return SimpleInMemoryRepository.class;
    }

    @RequiredArgsConstructor
    private class DefaultConstructorIdentifierGenerator<ID extends Identifier> implements IdentifierGenerator<ID> {

        private final Class<?> domainEntity;

        @Override
        public ID createIdentifier() {
            EntityInformation<?, ID> entityInformation = getEntityInformation(domainEntity);
            return getEntityIdentifierViaReflection(entityInformation.getIdType()).get();
        }

        @SuppressWarnings("unchecked")
        private Optional<ID> getEntityIdentifierViaReflection(Class<?> idType) {
            return ReflectionUtils.findConstructor(idType)
                .map(constructor -> (ID) BeanUtils.instantiateClass(constructor));
        }
    }
}
