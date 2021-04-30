package de.team7.swt.domain.infrastructure;

import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.Transactional;

/**
 * Callback interface to set up initial data on application startup.
 *
 * @author Vincent Nadoll
 * @see DelegatingDataInitializer
 */
@FunctionalInterface
public interface DataInitializer extends Ordered {

    int DEFAULT_ORDER = 0;

    /**
     * Called on application startup to trigger data initialization.
     *
     * @implNote Will run inside a transaction.
     */
    @Transactional
    void initialize();

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
