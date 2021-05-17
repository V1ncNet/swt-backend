package de.team7.swt.checkout.application;

import de.team7.swt.checkout.model.Order;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;

/**
 * {@link ApplicationEvent} being emitted if the given order was completed.
 *
 * @author Vincent Nadoll
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class OrderCompleted extends ApplicationEvent {

    Order order;

    private OrderCompleted(Order order) {
        super(order);
        this.order = order;
    }

    /**
     * Creates a new event indicating the given order was completed.
     *
     * @param order must not be {@literal null}
     * @return a new order completed event instance
     */
    public static OrderCompleted of(Order order) {
        Assert.notNull(order, "Order must not be null");
        return new OrderCompleted(order);
    }
}
