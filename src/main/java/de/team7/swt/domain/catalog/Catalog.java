package de.team7.swt.domain.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

/**
 * {@link org.springframework.data.repository.Repository} for managing {@link Product}s.
 *
 * @author Vincent Nadoll
 */
public interface Catalog<T extends Product> extends JpaRepository<T, Product.Id> {

    @Query(value = "select c from Product p join p.categories c group by c")
    Streamable<String> findAllCategories();

    @Query("select p from #{#entityName} p where :category member of p.categories")
    Streamable<T> findByCategory(String category);
}
