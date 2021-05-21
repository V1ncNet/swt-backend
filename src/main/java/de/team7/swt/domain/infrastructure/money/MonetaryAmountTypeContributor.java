package de.team7.swt.domain.infrastructure.money;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;

/**
 * <p>
 * A component {@link TypeContributor} providing an extension point to register custom
 * {@link org.hibernate.type.BasicType}s. This class will be instantiated by
 * {@link org.hibernate.boot.model.process.spi.MetadataBuildingProcess} using Java's
 * {@link java.util.ServiceLoader}-API.
 * </p>
 * <p>
 * Event though the {@literal org.hibernate.boot.registry.classloading.internal.AggregatedServiceLoader} throws an
 * exception on startup Hibernate type works fine. The exception only occurs when the application is started using the
 * {@code java -jar ...}-command.
 * </p>
 *
 * @author Vincent Nadoll
 * @see MonetaryAmountType
 */
public class MonetaryAmountTypeContributor implements TypeContributor {

    @Override
    public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        typeContributions.contributeType(MonetaryAmountType.getInstance());
    }
}
