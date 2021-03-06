package de.team7.swt.domain.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * An {@link ApplicationRunner} that detects and delegates to all beans of type {@link DataInitializer} allowing to
 * {@link DataInitializer#initialize()} them all at once. This component is {@link Ordered}, allowing other runners
 * executing before or after this one. A second or manual initialization attempt will fail.
 *
 * @author Vincent Nadoll
 */
@Component
public class DelegatingDataInitializer implements ApplicationRunner, Ordered {

    public static final int ORDER = HIGHEST_PRECEDENCE + 100;

    private final DataInitializerComposite initializers = new DataInitializerComposite();

    private boolean executed = false;

    /**
     * Adds {@link DataInitializer} beans to the underlying composite.
     */
    @Autowired(required = false)
    public void addInitializers(Collection<DataInitializer> initializers) {
        if (!CollectionUtils.isEmpty(initializers)) {
            this.initializers.addDataInitializers(initializers);
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        assertInitialExecution();

        initializers.initialize();

        executed = true;
    }

    private void assertInitialExecution() throws UnsupportedOperationException {
        if (executed) {
            throw new UnsupportedOperationException("This runner has already been executed");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
