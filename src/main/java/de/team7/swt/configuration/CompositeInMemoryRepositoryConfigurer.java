package de.team7.swt.configuration;

import de.team7.data.inmemory.repository.config.IdentifierRegistry;
import de.team7.data.inmemory.repository.config.InMemoryRepositoryConfigurer;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A composite configurer which does what a composite does... holding its own delegates.
 *
 * @author Vincent Nadoll
 */
class CompositeInMemoryRepositoryConfigurer implements InMemoryRepositoryConfigurer {

    private final Set<InMemoryRepositoryConfigurer> delegates = new HashSet<>();

    void addInMemoryRepositoryConfigurers(Collection<InMemoryRepositoryConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            delegates.addAll(configurers);
        }
    }

    @Override
    public void addIdentifiers(IdentifierRegistry registry) {
        delegates.forEach(delegates -> delegates.addIdentifiers(registry));
    }
}
