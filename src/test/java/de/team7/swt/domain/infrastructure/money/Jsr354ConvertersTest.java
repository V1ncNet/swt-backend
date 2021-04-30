package de.team7.swt.domain.infrastructure.money;

import de.team7.swt.domain.infrastructure.money.Jsr354Converters.MonetaryAmountToStringConverter;
import de.team7.swt.domain.infrastructure.money.Jsr354Converters.StringToMonetaryAmountConverter;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
class Jsr354ConvertersTest {

    StringToMonetaryAmountConverter toMonetaryAmountConverter = StringToMonetaryAmountConverter.INSTANCE;
    MonetaryAmountToStringConverter toStringConverter = MonetaryAmountToStringConverter.INSTANCE;

    @Test
    void convertingNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> toMonetaryAmountConverter.convert(null));
        assertThrows(IllegalArgumentException.class, () -> toStringConverter.convert(null));
    }

    @Test
    void convertingEmptyString_shouldReturnNull() {
        assertNull(toMonetaryAmountConverter.convert(""));
    }

    @Test
    void convertingValidArguments_shouldNotThrowException() {
        assertDoesNotThrow(() -> toMonetaryAmountConverter.convert("EUR1.00"));
        assertDoesNotThrow(() -> toStringConverter.convert(Money.of(1, "EUR")));
    }

    @Test
    void invalidStringConversion_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> toMonetaryAmountConverter.convert("foo"));
    }
}
