package de.team7.data.inmemory.repository.config;

/**
 * @author Vincent Nadoll
 */
public interface InMemoryRepositoryConfigurer {

    default void addIdentifiers(IdentifierRegistry registry) {
    }
}
