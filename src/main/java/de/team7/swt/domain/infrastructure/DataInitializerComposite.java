package de.team7.swt.domain.infrastructure;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link DataInitializer}-composite that delegates to one or more others.
 *
 * @author Vincent Nadoll
 */
class DataInitializerComposite implements DataInitializer {

    private final Set<DataInitializer> delegates = new HashSet<>();

    void addDataInitializers(Collection<DataInitializer> initializers) {
        if (!CollectionUtils.isEmpty(initializers)) {
            delegates.addAll(initializers);
        }
    }

    @Override
    public void initialize() {
        delegates.stream()
            .sorted(Comparator.comparing(DataInitializer::getOrder))
            .forEach(DataInitializer::initialize);
    }
}
