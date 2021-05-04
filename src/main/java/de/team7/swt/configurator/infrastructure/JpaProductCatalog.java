package de.team7.swt.configurator.infrastructure;

import de.team7.swt.domain.catalog.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Type;

/**
 * Implements methods defined in {@link ProductCatalog} by utilizing the JPAs {@link
 * javax.persistence.metamodel.Metamodel} to accumulate information about the {@link Product} entity and its subtypes.
 *
 * @author Vincent Nadoll
 */
@Repository
public class JpaProductCatalog extends SimpleJpaRepository<Product, Product.Id> implements ProductCatalog {

    private final EntityManager em;

    public JpaProductCatalog(EntityManager entityManager) {
        super(Product.class, entityManager);
        this.em = entityManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stream<Product> streamAllByEntityName(String entityName) {
        return em.getMetamodel()
            .getEntities()
            .stream()
            .filter(equalsIgnoreCase(entityName))
            .map(Type::getJavaType)
            .filter(Product.class::isAssignableFrom)
            .map(type -> (Class<Product>) type)
            .map(this::getQuery)
            .flatMap(TypedQuery::getResultStream);
    }

    private static Predicate<EntityType<?>> equalsIgnoreCase(String name) {
        return entityType -> StringUtils.equalsIgnoreCase(entityType.getName(), name);
    }

    /**
     * Creates a {@link TypedQuery} from the given domain class.
     *
     * @param domainClass must not be {@literal null}
     * @param <T>         the type of the product
     * @return a {@link TypedQuery} from the given domain class
     */
    protected <T extends Product> TypedQuery<T> getQuery(Class<T> domainClass) {
        return getQuery(null, domainClass, Sort.unsorted());
    }

    @Override
    public Stream<String> streamManagedProducts() {
        return em.getMetamodel().getEntities()
            .stream()
            .filter(this::isProductAssignableFrom)
            .map(EntityType::getName)
            .map(String::toLowerCase);
    }

    private boolean isProductAssignableFrom(EntityType<?> domainClass) {
        return Product.class.isAssignableFrom(domainClass.getJavaType());
    }
}
