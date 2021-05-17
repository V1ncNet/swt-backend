package de.team7.swt.checkout.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.team7.swt.checkout.application.OrderCompleted;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * An {@link AggregateRoot} representing an identifiable collection of items.
 *
 * @author Vincent Nadoll
 */
@Entity
@Table(name = "_order") // 'order' is reserved keyword in SQL
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Order extends AggregateRoot<Order.Id> implements Totalable<OrderItem> {

    @Getter
    @EmbeddedId
    @GeneratedValue(generator = "order-id")
    @GenericGenerator(name = "order-id", strategy = "dyob-id")
    @JsonUnwrapped
    private final Order.Id id;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<OrderItem> orderLine = new ArrayList<>();

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
     * Removes the given item from this order.
     *
     * @param item must not be {@literal null}
     */
    public void remove(OrderItem item) {
        Assert.notNull(item, "Order item must not be null");
        this.orderLine.remove(item);
    }

    /**
     * Completes this order.
     *
     * @see OrderCompleted
     */
    public void complete() {
        register(OrderCompleted.of(this));
    }

    @Override
    public Iterator<OrderItem> iterator() {
        return orderLine.iterator();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("orderLine", orderLine)
            .toString();
    }

    /**
     * Value object representing a order's primary identifier.
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
