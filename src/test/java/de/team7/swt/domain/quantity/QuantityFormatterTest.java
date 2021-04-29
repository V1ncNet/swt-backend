package de.team7.swt.domain.quantity;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
class QuantityFormatterTest {

    private static final QuantityFormatter formatter = QuantityFormatter.getInstance();

    @Test
    void singleton_shouldNotBeNull() {
        assertNotNull(formatter);
    }

    @Test
    void parseNullArguments_shouldNotTrowException() {
        assertDoesNotThrow(() -> formatter.parse(null, Locale.ROOT));
        assertDoesNotThrow(() -> formatter.parse("1l", null));
        assertDoesNotThrow(() -> formatter.parse(null, null));
    }

    @Test
    @SneakyThrows
    void parseEmptyValue_shouldReturnNoneQuantity() {
        Quantity parse = formatter.parse(null, Locale.ROOT);

        assertNotNull(parse);
        assertEquals(Quantity.NONE, parse);
    }

    @Test
    void unparsable_shouldThrowException() {
        assertThrows(ParseException.class, () -> formatter.parse("foo", Locale.ROOT));
        assertThrows(ParseException.class, () -> formatter.parse("d.1", Locale.ROOT));
    }

    @Test
    @SneakyThrows
    void formatter_shouldParseQuantityStrings() {
        assertEquals(Quantity.of(1.5, Metric.LITER), formatter.parse("1,5 l", Locale.GERMANY));
        assertEquals(Quantity.of(1.5, Metric.LITER), formatter.parse("1.5l", Locale.US));
        assertEquals(Quantity.of(1, Metric.UNIT), formatter.parse("1", Locale.US));
    }

    @Test
    void formatter_shouldPrintQuantities() {
        assertEquals("0", formatter.print(Quantity.NONE, Locale.ROOT));
        assertEquals("1", formatter.print(Quantity.of(1), Locale.ROOT));
        assertEquals("1l", formatter.print(Quantity.of(1, Metric.LITER), Locale.ROOT));
        assertEquals("1.5", formatter.print(Quantity.of(1.5), Locale.US));
        assertEquals("1,5", formatter.print(Quantity.of(1.5), Locale.GERMANY));
    }

    @Test
    void printNullValues_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> formatter.print(null, Locale.ROOT));
        assertThrows(IllegalArgumentException.class, () -> formatter.print(Quantity.NONE, null));
        assertThrows(IllegalArgumentException.class, () -> formatter.print(null, null));
    }
}
