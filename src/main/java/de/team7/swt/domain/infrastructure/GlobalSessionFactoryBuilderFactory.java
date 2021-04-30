package de.team7.swt.domain.infrastructure;

import de.team7.swt.domain.infrastructure.money.MonetaryAmountType;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.boot.spi.SessionFactoryBuilderFactory;
import org.hibernate.boot.spi.SessionFactoryBuilderImplementor;

/**
 * An application-wide {@link SessionFactoryBuilderFactory} providing an extension point to register custom {@link
 * org.hibernate.type.BasicType}s. This class will be instantiated by {@link org.hibernate.boot.internal.MetadataImpl}
 * using Java's {@link java.util.ServiceLoader}-API.
 *
 * @author Vincent Nadoll
 * @see MonetaryAmountType
 */
public class GlobalSessionFactoryBuilderFactory implements SessionFactoryBuilderFactory {

    @Override
    @SuppressWarnings("deprecation")
    public SessionFactoryBuilder getSessionFactoryBuilder(MetadataImplementor metadata,
                                                          SessionFactoryBuilderImplementor defaultBuilder) {
        metadata.getTypeResolver().registerTypeOverride(MonetaryAmountType.getInstance());
        return defaultBuilder;
    }
}
