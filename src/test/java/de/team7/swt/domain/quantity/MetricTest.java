package de.team7.swt.domain.quantity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
class MetricTest {

    @Test
    void initializeNullArgument_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Metric.from(null));
    }

    @Test
    void unsupportedAbbrev_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Metric.from("foo"));
    }

    @Test
    void metricFromAbbrev_shouldCreateNewMetric() {
        assertEquals(Metric.LITER, Metric.from("l"));
        assertEquals(Metric.LITER, Metric.from("  l "));

        assertEquals(Metric.UNIT, Metric.from(""));
        assertEquals(Metric.UNIT, Metric.from("  "));
    }
}

