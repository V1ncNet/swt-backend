package de.team7.swt.configurator.infrastructure;

import de.team7.swt.domain.catalog.Catalog;
import de.team7.swt.domain.catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.stream.Stream;

/**
 * {@link org.springframework.data.repository.Repository} that provides a more generic way to access {@link Product}
 * subtypes.
 *
 * @author Vincent Nadoll
 * @see JpaProductCatalog
 */
@NoRepositoryBean
public interface ProductCatalog extends Catalog<Product>, JpaRepository<Product, Product.Id> {

    /**
     * Returns all products by the given entity name.
     *
     * @param entityName must not be {@literal null}
     * @return a stream of all products matching the given entity name
     * @see #streamManagedProducts()
     * @see javax.persistence.Entity#name()
     */
    Stream<Product> streamAllByEntityName(String entityName);

    /**
     * Returns all sub-product entity names.
     *
     * @return a stream of entity names which inherit for the {@link Product} class
     * @see #streamAllByEntityName(String)
     * @see javax.persistence.Entity#name()
     */
    Stream<String> streamManagedProducts();
}
