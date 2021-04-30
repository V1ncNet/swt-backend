package de.team7.swt.domain.infrastructure.money;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.MonetaryAmount;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vincent Nadoll
 */
class MonetaryAmountAttributeConverterTest {

    private MonetaryAmountAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new MonetaryAmountAttributeConverter();
    }

    @Test
    void convertNullArguments_ReturnNullValues() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
        assertNull(converter.convertToEntityAttribute(""));
    }

    @Test
    void moneyConversion_shouldConvertToString() {
        String eur = converter.convertToDatabaseColumn(Money.of(1, "EUR"));

        assertNotNull(eur);
        assertTrue(eur.contains("1.00"));
        assertTrue(eur.contains("EUR"));
    }

    @Test
    void stringConversion_shouldConvertToMoney() {
        MonetaryAmount amount = converter.convertToEntityAttribute("EUR1.00");

        assertNotNull(amount);
    }
}
