package de.team7.swt.checkout.model;

import de.team7.swt.checkout.application.OrderCompletionReport;
import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This unchecked {@link RuntimeException} is thrown in case an order could not be completed successfully.
 *
 * @author Vincent Nadoll
 * @see OrderCompletionReport
 */
@Getter
@ResponseStatus
public class OrderCompletionFailure extends RuntimeException {

    private final OrderCompletionReport report;

    /**
     * Creates a new instance with given failed order.
     *
     * @param order must not be {@literal null}
     */
    public OrderCompletionFailure(Order order) {
        super("Order could not be completed successfully");

        Assert.notNull(order, "Order must not be null");

        this.report = OrderCompletionReport.failed(order);
    }

    /**
     * Creates a new instance with given report.
     *
     * @param report must no be {@literal null}
     * @throws IllegalArgumentException in case the given report doesn't contain errors
     */
    public OrderCompletionFailure(OrderCompletionReport report) {
        super("Order could not be completed successfully");

        Assert.notNull(report, "Order completion report must not be null");
        Assert.isTrue(report.hasErrors(), "Order completion must contain errors");

        this.report = report;
    }
}
