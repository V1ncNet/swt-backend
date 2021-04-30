package de.team7.swt.domain.infrastructure.money;

import org.hibernate.type.descriptor.WrapperOptions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
class MonetaryAmountTypeDescriptorTest {

    WrapperOptions wrapperOptions;
    MonetaryAmountTypeDescriptor descriptor;

    @BeforeEach
    void setUp() {
        wrapperOptions = mock(WrapperOptions.class);
        descriptor = new MonetaryAmountTypeDescriptor();
    }

    @Test
    void singleton_shouldNotBeNull() {
        assertNotNull(MonetaryAmountTypeDescriptor.getInstance());
    }

    @Test
    void initializeNullArguments_shouldReturnNull() {
        assertNull(descriptor.fromString(""));
        assertNull(descriptor.fromString(null));
        assertNull(descriptor.wrap(null, null));
        assertNull(descriptor.wrap("", null));
        assertNull(descriptor.unwrap(null, Money.class, wrapperOptions));
        assertNull(descriptor.unwrap(null, String.class, wrapperOptions));
    }

    @Test
    void validString_shouldConvertSuccessfully() {
        MonetaryAmount amount = descriptor.fromString("EUR1.00");

        assertNotNull(amount);
    }

    @Test
    void invalidString_shouldThrowException() {
        assertThrows(MonetaryParseException.class, () -> descriptor.fromString("foo"));
    }

    @Test
    void unwrap_shouldConvertSupportedTypes() {
        assertNotNull(descriptor.unwrap(Money.of(1, "EUR"), Money.class, wrapperOptions));
    }

    @Test
    void unwrapString_shouldConvertSupportedTypes() {
        String eur = descriptor.unwrap(Money.of(1, "EUR"), String.class, wrapperOptions);
        assertTrue(StringUtils.hasText(eur));
    }

    @Test
    void wrapMonetaryAmount_shouldConvertToSupportedTypes() {
        Money eur = Money.of(1, "EUR");
        MonetaryAmount result = descriptor.wrap(eur, wrapperOptions);

        assertNotNull(result);
        assertEquals(eur, result);
    }

    @Test
    void wrapString_shouldConvertToSupportedTypes() {
        assertNotNull(descriptor.wrap("EUR1.00", wrapperOptions));
    }
}
