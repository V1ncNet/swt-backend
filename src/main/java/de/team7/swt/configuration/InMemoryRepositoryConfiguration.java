package de.team7.swt.configuration;

import de.team7.data.inmemory.repository.config.IdentifierMapping;
import de.team7.data.inmemory.repository.config.IdentifierRegistry;
import de.team7.data.inmemory.repository.config.InMemoryRepositoryConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * A configuration component which performs further setup for the in-memory repositories.
 *
 * @author Vincent Nadoll
 */
@Configuration
class InMemoryRepositoryConfiguration {

    private final CompositeInMemoryRepositoryConfigurer configurers = new CompositeInMemoryRepositoryConfigurer();

    @Autowired(required = false)
    public void setConfigurers(Collection<InMemoryRepositoryConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addInMemoryRepositoryConfigurers(configurers);
        }
    }

    @Bean
    public IdentifierMapping identifierMapping() {
        IdentifierRegistry registry = new IdentifierRegistry();
        configurers.addIdentifiers(registry);
        return registry.get();
    }
}
