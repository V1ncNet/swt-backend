package de.team7.swt.domain.catalog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.team7.swt.domain.quantity.Metric;
import de.team7.swt.domain.quantity.MetricMismatchException;
import de.team7.swt.domain.quantity.Quantity;
import de.team7.swt.domain.shared.AggregateRoot;
import de.team7.swt.domain.shared.Identifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.money.MonetaryAmount;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.PrePersist;

/**
 * Base class for a product.
 *
 * @author Julian Albrecht
 * @author Vincent Nadoll
 */
@Entity
public class Product extends AggregateRoot<Product.Id> implements Comparable<Product> {

    @Getter
    @EmbeddedId
    @GeneratedValue(generator = "product-id")
    @GenericGenerator(name = "product-id", strategy = "dyob-id")
    @JsonUnwrapped
    private final Product.Id id;

    @Getter
    @Setter
    @NonNull
    private String name;

    @Getter
    @Setter
    @NonNull
    @Convert(disableConversion = true)
    @Type(type = "de.team7.swt.domain.infrastructure.money.MonetaryAmountType")
    private MonetaryAmount price;

    @JsonProperty
    @Enumerated(EnumType.STRING)
    private final Metric metric;

    @ElementCollection(fetch = FetchType.EAGER)
    private final Set<String> categories = new HashSet<>();

    /**
     * Creates a new product with given ID, name and price.
     *
     * @param id    can be {@literal null}
     * @param name  must not be {@literal null} nor empty
     * @param price must not be {@literal null}
     */
    protected Product(Product.Id id, String name, MonetaryAmount price) {
        this(id, name, price, Metric.UNIT);
    }

    /**
     * Creates a new product with given name and price.
     *
     * @param name  must not be {@literal null} nor empty
     * @param price must not be {@literal null}
     */
    public Product(String name, MonetaryAmount price) {
        this(name, price, Metric.UNIT);
    }

    /**
     * Creates a new product with given name, price and metric.
     *
     * @param name   must not be {@literal null} nor empty
     * @param price  must not be {@literal null}
     * @param metric must not be {@literal null}
     */
    public Product(String name, MonetaryAmount price, Metric metric) {
        this(null, name, price, metric);
    }

    /**
     * Creates a new product with given ID, name, price and metric.
     *
     * @param id     can be {@literal null}
     * @param name   must not be {@literal null} nor empty
     * @param price  must not be {@literal null}
     * @param metric must not be {@literal null}
     */
    protected Product(Product.Id id, String name, MonetaryAmount price, Metric metric) {
        Assert.hasText(name, "Name must not be empty");
        Assert.notNull(price, "Price must not be null");
        Assert.notNull(metric, "Metric must not be null");

        this.id = id;
        this.name = name;
        this.price = price;
        this.metric = metric;
    }

    /**
     * Returns the categories this product is assigned to.
     *
     * @return all categories; never {@literal null}
     */
    @JsonIgnore
    public Streamable<String> getCategories() {
        return Streamable.of(Collections.unmodifiableSet(categories));
    }

    /**
     * Adds this product to the given category.
     *
     * @param category must not be {@literal null} nor empty
     * @return {@literal true} if this product is not already assigned to the category; {@literal false} otherwise
     */
    public final boolean add(String category) {
        Assert.hasText(category, "Category must not be null");
        return categories.add(category);
    }

    /**
     * Removes this product form the given category.
     *
     * @param category must not be {@literal null} nor empty
     * @return {@literal true} if this product where assigned to the category; {@literal false} otherwise
     */
    public final boolean remove(String category) {
        Assert.hasText(category, "Category must not be null");
        return categories.remove(category);
    }

    /**
     * Verifies the given {@link Quantity} to match the one supported by this product.
     *
     * @param quantity must not be {@literal null}
     * @throws MetricMismatchException in case this product doesn't support the given {@link Quantity}
     */
    public void verify(Quantity quantity) throws MetricMismatchException {
        if (!supports(quantity)) {
            throw new MetricMismatchException(
                String.format("Product %s does not support quantity %s using metric %s!",
                    this, quantity, quantity.getMetric())
            );
        }
    }

    /**
     * Returns whether this product supports the given {@link Quantity}.
     *
     * @param quantity must not be {@literal null}
     * @return {@literal true} if the {@link Quantity} is supported; {@literal false} otherwise
     */
    public boolean supports(Quantity quantity) {
        Assert.notNull(quantity, "Quantity must not be null");
        return quantity.isCompatibleWith(metric);
    }

    /**
     * Creates a new {@link Quantity} instance of the given amount and this product's underlying {@link Metric}.
     */
    public Quantity from(long amount) {
        return Quantity.of(amount, metric);
    }

    /**
     * Creates a new {@link Quantity} instance of the given amount and this product's underlying {@link Metric}.
     */
    public Quantity from(double amount) {
        return Quantity.of(amount, metric);
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Product other) {
        return Objects.compare(this.name, other.name, String::compareToIgnoreCase);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("name", name)
            .append("price", price)
            .append("categories", categories)
            .append("metric", metric)
            .toString();
    }

    @PrePersist
    void prePersist() {
        Assert.notNull(metric,
            "Metric must not be null. Make sure you've created the product with a non default constructor.");
    }

    protected Product() {
        this.id = null;
        this.metric = null;
    }

    /**
     * Value object representing a product's primary identifier.
     *
     * @author Vincent Nadoll
     */
    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Id extends Identifier {
        public Id(@NonNull UUID id) {
            super(id);
        }
    }
}
