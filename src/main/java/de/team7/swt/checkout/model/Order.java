package de.team7.swt.checkout.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import de.team7.swt.domain.shared.AggregateRoot;
import de.team7.swt.domain.shared.Identifier;
import lombok.Getter;
import org.javamoney.moneta.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.money.MonetaryAmount;

/**
 * An {@link AggregateRoot} representing an identifiable collection of items.
 *
 * @author Vincent Nadoll
 */
@Getter
public class Order extends AggregateRoot<Order.Id> {

    @JsonUnwrapped
    private final Order.Id id;

    private final List<OrderItem> orderLine = new ArrayList<>();

    public Order() {
        this.id = new Id();
    }

    /**
     * Adds a new item from given {@link Product} and {@link Quantity} to this order.
     *
     * @param product  must not be {@literal null}
     * @param quantity must not be {@literal null}
     * @return added {@link OrderItem}
     */
    public OrderItem addItem(Product product, Quantity quantity) {
        OrderItem item = new OrderItem(product, quantity);
        this.orderLine.add(item);
        return item;
    }

    /**
     * Sums up the hole order line.
     *
     * @return the total amount of all order items.
     */
    public MonetaryAmount getTotal() {
        return orderLine.stream()
            .map(OrderItem::getPrice)
            .reduce(MonetaryAmount::add)
            .orElse(Money.of(0, "EUR"));
    }

    /**
     * Value object representing a order's primary identifier.
     *
     * @author Vincent Nadoll
     */
    public static final class Id extends Identifier {
        public Id() {
            super(UUID.randomUUID());
        }
    }
}
