package de.team7.swt.domain.quantity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
class StringToQuantityConverterTest {

    private StringToQuantityConverter converter;

    @BeforeEach
    void setUp() {
        QuantityFormatter formatter = new QuantityFormatter();
        converter = new StringToQuantityConverter(formatter);
    }

    @Test
    void unparseableSource_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("foo"));
        assertThrows(IllegalArgumentException.class, () -> converter.convert("1.dl"));
    }

    @Test
    void parsableSource_shouldBeConverted() {
        assertEquals(Quantity.NONE, converter.convert(null));
        assertEquals(Quantity.of(1.5, Metric.LITER), converter.convert("1.5l"));
        assertEquals(Quantity.of(1, Metric.LITER), converter.convert("1l"));
        assertEquals(Quantity.of(1, Metric.UNIT), converter.convert("1"));
        assertEquals(Quantity.of(0), converter.convert("0"));
    }
}
