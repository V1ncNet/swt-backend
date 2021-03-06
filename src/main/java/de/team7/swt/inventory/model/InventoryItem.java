package de.team7.swt.inventory.model;

import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import de.team7.swt.domain.shared.AggregateRoot;
import de.team7.swt.domain.shared.Identifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.Assert;

import java.util.UUID;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * An inventory item associates a {@link Product} with a {@link Quantity} to keep track of how many items per product
 * are available.
 *
 * @author Vincent Nadoll
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class InventoryItem extends AggregateRoot<InventoryItem.Id> {

    @EmbeddedId
    @GeneratedValue(generator = "inventoryItem-id")
    @GenericGenerator(name = "inventoryItem-id", strategy = "dyob-id")
    private final InventoryItem.Id id;

    @OneToOne
    @JoinColumn(unique = true, updatable = false)
    private Product product;

    private Quantity quantity;

    /**
     * Creates a new item with given product and quantity.
     *
     * @param product  must not be {@literal null}
     * @param quantity must not be {@literal null}
     * @throws de.team7.swt.domain.quantity.MetricMismatchException in case the given {@link Product} doesn't support
     *                                                              the given {@link Quantity}
     */
    public InventoryItem(Product product, Quantity quantity) {
        Assert.notNull(product, "Product must not be null");
        Assert.notNull(quantity, "Quantity must not be null");
        product.verify(quantity);

        this.id = null;
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Increases the amount of products by the given quantity.
     *
     * @param quantity must not be {@literal null}
     * @throws de.team7.swt.domain.quantity.MetricMismatchException in case this item's {@link Product} doesn't support
     *                                                              the given {@link Quantity}
     */
    public void increase(Quantity quantity) {
        product.verify(quantity);

        this.quantity = this.quantity.add(quantity);
    }

    /**
     * Decreases the amount of products by the given quantity.
     *
     * @param quantity must not be {@literal null}
     * @throws IllegalArgumentException                             in case this item's {@link Product} doesn't contain
     *                                                              sufficient amount
     * @throws de.team7.swt.domain.quantity.MetricMismatchException in case this item's {@link Product} doesn't support
     *                                                              the given {@link Quantity}
     */
    public void decrease(Quantity quantity) throws InsufficientQuantityException {
        verifySufficientStock(quantity);
        product.verify(quantity);

        this.quantity = this.quantity.subtract(quantity);
    }

    private void verifySufficientStock(Quantity quantity) throws InsufficientQuantityException {
        if (!hasSufficientStock(quantity)) {
            throw new InsufficientQuantityException("Insufficient quantity! Make sure there's a sufficient amount of"
                + "products in stock.", this);
        }
    }

    /**
     * Returns whether this item's product having a sufficient amount in stock.
     *
     * @param quantity can be {@literal null}
     * @return {@literal true} if this item has a sufficient amount of products in stock; {@literal false} otherwise
     * @throws de.team7.swt.domain.quantity.MetricMismatchException in case the quantities' metrics didn't match
     */
    public boolean hasSufficientStock(Quantity quantity) {
        return !this.quantity.subtract(quantity).isNegative();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("product", product)
            .append("quantity", quantity)
            .toString();
    }

    /**
     * Value object representing an inventory item's primary identifier.
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
