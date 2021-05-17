package de.team7.swt.checkout.application;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.team7.swt.checkout.model.OrderItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Objects;

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

    @Nullable
    String message;

    /**
     * Creates a new completion indicating a successful verification of the given order item.
     *
     * @param item must not be {@literal null}
     * @return a new completion
     */
    public static OrderItemCompletion success(OrderItem item) {
        Assert.notNull(item, "Order item must not be null");
        return new OrderItemCompletion(item, CompletionStatus.SUCCEEDED, null);
    }

    /**
     * Creates a new completion indicating a skipped verification of the given order item.
     *
     * @param item must not be {@literal null}
     * @return a new completion
     */
    public static OrderItemCompletion skip(OrderItem item) {
        Assert.notNull(item, "Order item must not be null");
        return new OrderItemCompletion(item, CompletionStatus.SUCCEEDED, "The order item has been skipped.");
    }

    /**
     * Creates a new completion indicating a erroneous verification of the given order item.
     *
     * @param item must not be {@literal null}
     * @return a new completion
     */
    public static OrderItemCompletion error(OrderItem item, String message) {
        Assert.notNull(item, "Order item must not be null");
        return new OrderItemCompletion(item, CompletionStatus.FAILED, message);
    }

    /**
     * Indicates whether this completion has failed.
     *
     * @return {@literal true} if this completion is successful; {@literal false} otherwise
     */
    public boolean hasFailed() {
        return Objects.equals(CompletionStatus.FAILED, status);
    }
}
