package de.team7.swt.domain.infrastructure.money;

import de.team7.swt.domain.infrastructure.money.Jsr354Converters.MonetaryAmountToBigDecimalConverter;
import de.team7.swt.domain.infrastructure.money.Jsr354Converters.MonetaryAmountToStringConverter;
import de.team7.swt.domain.infrastructure.money.Jsr354Converters.StringToMonetaryAmountConverter;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import javax.money.MonetaryAmount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
class Jsr354ConvertersTest {

    StringToMonetaryAmountConverter toMonetaryAmountConverter = StringToMonetaryAmountConverter.INSTANCE;
    MonetaryAmountToStringConverter toStringConverter = MonetaryAmountToStringConverter.INSTANCE;
    MonetaryAmountToBigDecimalConverter toBigDecimalConverter = MonetaryAmountToBigDecimalConverter.INSTANCE;

    @Test
    void convertingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> toMonetaryAmountConverter.convert(null));
        assertThrows(IllegalArgumentException.class, () -> toStringConverter.convert(null));
        assertThrows(IllegalArgumentException.class, () -> toBigDecimalConverter.convert(null));
    }

    @Test
    void convertingEmptyString_shouldReturnNull() {
        assertNull(toMonetaryAmountConverter.convert(""));
    }

    @Test
    void invalidStringConversion_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> toMonetaryAmountConverter.convert("foo"));
    }

    @Test
    void string_shouldConvertToString() {
        MonetaryAmount value = toMonetaryAmountConverter.convert("EUR1.00");

        assertNotNull(value);
        assertEquals(BigDecimal.valueOf(1), value.getNumber().numberValue(BigDecimal.class));
        assertEquals("EUR", value.getCurrency().getCurrencyCode());
    }

    @Test
    void monetaryAmount_shouldConvertToString() {
        String value = toStringConverter.convert(Money.of(2.99, "EUR"));

        assertNotNull(value);
        assertEquals("EUR 2.99", value);
    }

    @Test
    void monetaryAmount_shouldConvertToBigDecimal() {
        BigDecimal value = toBigDecimalConverter.convert(Money.of(2.99, "EUR"));

        assertNotNull(value);
        assertEquals(BigDecimal.valueOf(2.99), value);
    }
}
