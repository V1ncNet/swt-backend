package de.team7.swt.domain.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@link org.springframework.data.repository.Repository} for managing {@link Product}s.
 *
 * @author Vincent Nadoll
 */
public interface Catalog<T extends Product> extends JpaRepository<T, Product.Id> {
}
