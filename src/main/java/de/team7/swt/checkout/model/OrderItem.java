package de.team7.swt.checkout.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import de.team7.swt.domain.shared.Identifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.util.Assert;

import java.util.UUID;
import javax.money.MonetaryAmount;

/**
 * An {@link de.team7.swt.domain.shared.Entity} representing {@link Product} and {@link Quantity} that is intended to be
 * purchased as part of an {@link Order}.
 *
 * @author Vincent Nadoll
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class OrderItem extends de.team7.swt.domain.shared.Entity<OrderItem.Id> {

    @JsonUnwrapped
    private final OrderItem.Id id;

    @JsonIgnore
    private Product.Id productId;

    private String productName;

    @NumberFormat(pattern = "0.00 ¤")
    private MonetaryAmount price;

    private Quantity quantity;

    /**
     * Creates a new order item with given product and quantity.
     *
     * @param product  must not be {@literal null}
     * @param quantity must not be {@literal null}
     */
    OrderItem(Product product, Quantity quantity) {
        this(new Id(), product, quantity);
    }

    /**
     * Creates a new order item with given ID, product and quantity.
     *
     * @param id       must not be {@literal null}
     * @param product  must not be {@literal null}
     * @param quantity must not be {@literal null}
     */
    protected OrderItem(OrderItem.Id id, Product product, Quantity quantity) {
        Assert.notNull(id, "Item ID must not be null");
        Assert.notNull(product, "Product must not be null");
        Assert.notNull(quantity, "Quantity must not be null");
        product.verify(quantity);

        this.id = id;
        this.productId = product.getId();
        this.productName = product.getName();
        this.price = product.getPrice().multiply(quantity.getAmount());
        this.quantity = quantity;
    }

    @JsonGetter("productId")
    private String getUnwrappedProductId() {
        return productId.toString();
    }


    /**
     * Value object representing an order item's primary identifier.
     *
     * @author Vincent Nadoll
     */
    public static final class Id extends Identifier {
        public Id() {
            super(UUID.randomUUID());
        }
    }
}
