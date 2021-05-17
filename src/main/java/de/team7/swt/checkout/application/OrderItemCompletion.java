package de.team7.swt.checkout.application;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.team7.swt.checkout.model.OrderItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.util.Assert;

/**
 * Value object encapsulating an order items completion status and optional error message.
 *
 * @author Vincent Nadoll
 * @see OrderCompletionReport
 */
@Value
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class OrderItemCompletion {

    @JsonUnwrapped
    OrderItem item;
    CompletionStatus status;

    /**
     * Creates a new completion indicating a successful verification of the given order item.
     *
     * @param item must not be {@literal null}
     * @return a new completion
     */
    public static OrderItemCompletion success(OrderItem item) {
        Assert.notNull(item, "Order item must not be null");
        return new OrderItemCompletion(item, CompletionStatus.SUCCEEDED);
    }
}
