package de.team7.swt.domain.infrastructure;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * An internal kind-of-composite component triggering the initialization of all {@link DataInitializer} instances
 * registered in the {@link org.apache.catalina.core.ApplicationContext}. This class is marked {@literal public} to
 * allow access to the {@link #ORDER} constant, other components can use to execute initialization after this one. A
 * second or manual initialization attempt will fail.
 *
 * @author Vincent Nadoll
 */
@Component
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DataInitializerRunner implements ApplicationRunner, Ordered {

    public static final int ORDER = HIGHEST_PRECEDENCE + 100;

    private final Set<DataInitializer> delegates = new HashSet<>();

    private boolean executed = false;

    @Autowired
    void setDataInitializers(Collection<DataInitializer> initializers) {
        if (!CollectionUtils.isEmpty(initializers)) {
            delegates.addAll(initializers);
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        assertInitialExecution();

        delegates.stream()
            .sorted(Comparator.comparing(DataInitializer::getOrder))
            .forEach(DataInitializer::initialize);

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

    /**
     * Noop implementation to make sure there is always at least one {@link DataInitializer} implementation to
     * {@link Autowired autowire}. Without, startup would fail with Spring complaining about missing candidates.
     *
     * @author Vincent Nadoll
     */
    @Component
    static class NoopDataInitializer implements DataInitializer {
        @Override
        public void initialize() {
        }
    }
}
