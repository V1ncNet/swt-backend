package de.team7.swt.domain.infrastructure.quantity;

import de.team7.swt.domain.quantity.Metric;
import de.team7.swt.domain.quantity.Quantity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionFailedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
@SpringBootTest
class StringToQuantityConverterTest {

    @Autowired
    private StringToQuantityConverter converter;

    @Test
    void unparsableSource_shouldThrowException() {
        assertThrows(ConversionFailedException.class, () -> converter.convert("foo"));
        assertThrows(ConversionFailedException.class, () -> converter.convert("1.dl"));
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
