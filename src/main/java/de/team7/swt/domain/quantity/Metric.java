package de.team7.swt.domain.quantity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents a unit of measurement.
 *
 * @author Vincent Nadoll
 * @see Quantity
 */
@Getter
@RequiredArgsConstructor
public enum Metric {
    LITER("l"),
    UNIT(""),
    ;

    private final String abbreviation;

    public static Metric from(String abbreviation) {
        Assert.notNull(abbreviation, "Abbreviation must not be null");
        String trimmed = abbreviation.trim();
        return Arrays.stream(values())
            .filter(equals(trimmed))
            .findFirst()
            .orElseThrow(unsupported(abbreviation));
    }

    public static Predicate<Metric> equals(String abbreviation) {
        return metric -> Objects.equals(metric.abbreviation, abbreviation);
    }

    private static Supplier<IllegalArgumentException> unsupported(String abbreviation) {
        return () -> new IllegalArgumentException(String.format("Unsupported abbreviation [%s]", abbreviation));
    }
}
