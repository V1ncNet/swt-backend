package de.team7.swt.domain.quantity;

import lombok.NoArgsConstructor;

/**
 * This exception is thrown in case a measurable expects a certain {@link Metric} but got an incompatible.
 *
 * @author Vincent Nadoll
 */
@NoArgsConstructor
public class MetricMismatchException extends RuntimeException {

    public MetricMismatchException(String message) {
        super(message);
    }

    /**
     * Convenience constructor to achieve consistent error messages.
     */
    public MetricMismatchException(Metric expected, Metric actual) {
        this("", expected, actual);
    }

    /**
     * Convenience constructor to achieve consistent error messages.
     */
    public MetricMismatchException(String message, Metric expected, Metric actual) {
        super(String.format(
            "%s\nExpected metric [%s] but got [%s]",
            message, expected.name(), actual.name()
        ));
    }
}
