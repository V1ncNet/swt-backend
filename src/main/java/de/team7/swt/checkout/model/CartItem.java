package de.team7.swt.checkout.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import de.team7.swt.domain.shared.Identifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.util.Assert;

import java.util.UUID;
import javax.money.MonetaryAmount;

/**
 * Immutable entity encapsulating a {@link Product} and its {@link Quantity}.
 *
 * @author Vincent Nadoll
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItem {

    @JsonIgnore
    Id id;

    @JsonUnwrapped
    Product product;

    @NumberFormat(pattern = "0.00 ¤")
    MonetaryAmount price;

    Quantity quantity;

    /**
     * Creates a new cart items with given product and quantity.
     *
     * @param product  must not be {@literal null}
     * @param quantity must not be {@literal null}
     */
    CartItem(Product product, Quantity quantity) {
        this(new Id(), product, quantity);
    }

    private CartItem(Id id, Product product, Quantity quantity) {
        Assert.notNull(product, "Product must not be null");
        Assert.notNull(quantity, "Quantity must not be null");
        product.verify(quantity);

        this.id = id;
        this.product = product;
        this.price = product.getPrice().multiply(quantity.getAmount());
        this.quantity = quantity;
    }

    /**
     * Creates a new cart item instance that has the given quantity added to this one.
     *
     * @param quantity must not be {@literal null}
     * @return a new cart item instance
     */
    final CartItem add(Quantity quantity) {
        Assert.notNull(quantity, "Quantity must not be null");
        return new CartItem(this.id, this.product, this.quantity.add(quantity));
    }

    /**
     * Creates a new {@link OrderItem} from this item.
     *
     * @return a new order item instance
     */
    final OrderItem toOrderItem() {
        return new OrderItem(product, quantity);
    }

    /**
     * Value object representing a cart item's primary identifier.
     *
     * @author Vincent Nadoll
     */
    public static final class Id extends Identifier {
        public Id() {
            super(UUID.randomUUID());
        }
    }
}
