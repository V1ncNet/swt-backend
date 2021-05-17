package de.team7.swt.checkout.application;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.team7.swt.checkout.model.Order;
import de.team7.swt.checkout.model.OrderCompletionFailure;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;

/**
 * Value object accumulating {@link OrderItemCompletion}s to determine an order's completion.
 *
 * @author Vincent Nadoll
 */
@Value
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCompletionReport implements Streamable<OrderItemCompletion> {

    Order order;
    CompletionStatus status;

    @JsonIgnore
    Streamable<OrderItemCompletion> completions;

    @JsonGetter
    private List<OrderItemCompletion> getCompletions() {
        return completions.toList();
    }

    /**
     * Creates a new report indicating a successful verification of the given order and its items.
     *
     * @param order must not be {@literal null}
     * @return a new report
     */
    public static OrderCompletionReport success(Order order) {
        Assert.notNull(order, "Order must not be null");
        return OrderCompletionReport.of(order, order.map(OrderItemCompletion::success));
    }

    /**
     * Creates a new report indicating a failed verification of the given order.
     *
     * @param order must not be {@literal null}
     * @return a new report
     */
    public static OrderCompletionReport failed(Order order) {
        return new OrderCompletionReport(order, CompletionStatus.FAILED, Streamable.empty());
    }

    /**
     * Creates a new report verifying the given order and completions.
     *
     * @param order       must not be {@literal null}
     * @param completions must not be {@literal null}
     * @return a new report
     */
    public static OrderCompletionReport of(Order order, Streamable<OrderItemCompletion> completions) {
        Assert.notNull(order, "Order must not be null");
        Assert.notNull(completions, "Order item completions must not be null");
        return new OrderCompletionReport(order, getStatus(completions), completions);
    }

    private static CompletionStatus getStatus(Streamable<OrderItemCompletion> completions) {
        return hasErrors(completions)
            ? CompletionStatus.FAILED
            : CompletionStatus.SUCCEEDED;
    }

    /**
     * Indicates whether this report contains any errors.
     *
     * @return {@literal true} if this report contains errors; {@literal false} otherwise
     */
    public boolean hasErrors() {
        return hasErrors(completions);
    }

    private static boolean hasErrors(Streamable<OrderItemCompletion> line) {
        return line.stream().anyMatch(OrderItemCompletion::hasFailed);
    }

    /**
     * Verifies this report doesn't contain errors.
     *
     * @throws OrderCompletionFailure in case this report contains errors
     */
    public void verify() throws OrderCompletionFailure {
        if (hasErrors()) {
            throw new OrderCompletionFailure(this);
        }
    }

    @Override
    public Iterator<OrderItemCompletion> iterator() {
        return completions.iterator();
    }

    @JsonIgnore
    @Override
    public boolean isEmpty() {
        return Streamable.super.isEmpty();
    }
}
