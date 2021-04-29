package de.team7.swt.domain.infrastructure;

import de.team7.swt.domain.infrastructure.identifier.IdentifierGenerator;
import org.hibernate.jpa.spi.IdentifierGeneratorStrategyProvider;

import java.util.Collections;
import java.util.Map;

/**
 * An application-wide Hibernate ID generator provider, providing a mapping between strategy names and
 * {@link org.hibernate.id.IdentifierGenerator} types. This class is will be instantiated by
 * {@link org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl} based on the configured
 * {@link org.hibernate.jpa.AvailableSettings#IDENTIFIER_GENERATOR_STRATEGY_PROVIDER} fully qualified class name.
 *
 * @author Vincent Nadoll
 * @see org.hibernate.id.IdentifierGenerator
 * @see de.team7.swt.domain.shared.Identifier
 */
@SuppressWarnings("unused")
public class GlobalIdentifierGeneratorStrategyProvider implements IdentifierGeneratorStrategyProvider {

    @Override
    public Map<String, Class<?>> getStrategies() {
        return Collections.singletonMap("dyob-id", IdentifierGenerator.class);
    }
}
