package de.team7.swt.domain.infrastructure.money;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import de.team7.swt.domain.infrastructure.money.Jsr354Converters.MonetaryAmountToBigDecimalConverter;
import lombok.Value;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.RoundedMoney;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import javax.money.MonetaryAmount;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vincent Nadoll
 */
class MonetaryAmountDeserializerTest<T extends MonetaryAmount> {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Test
    void moduleRegistry_shouldProvideDefaultMonetaryAmountImplementation() throws IOException {
        MonetaryAmount value = mapper.readValue(resourceStream("price.json"), MonetaryAmount.class);

        assertNotNull(value);
        assertTrue(value instanceof Money);
    }

    @ParameterizedTest
    @MethodSource("moneyTypes")
    void missingProperty_shouldThrowException(Class<T> moneyType) {
        assertThrows(JsonProcessingException.class, () -> mapper.readValue(resourceStream("price_onlyAmount.json"), moneyType));
        assertThrows(JsonProcessingException.class, () -> mapper.readValue(resourceStream("price_onlyCurrency.json"), moneyType));
    }

    @ParameterizedTest
    @MethodSource("moneyTypes")
    void shouldDeserializeToCorrectType(Class<T> moneyType) throws IOException {
        MonetaryAmount value = mapper.readValue(resourceStream("price.json"), moneyType);

        assertTrue(moneyType.isAssignableFrom(value.getClass()));
    }

    @ParameterizedTest
    @MethodSource("moneyTypes")
    void validPriceJson_shouldDeserialize(Class<T> moneyType) throws IOException {
        MonetaryAmount value = mapper.readValue(resourceStream("price.json"), moneyType);

        assertMoneyValues(2.99, "EUR", value);
    }

    @ParameterizedTest
    @MethodSource("moneyTypes")
    void highNumberOfFractionDigits_shouldDeserialize(Class<T> moneyType) throws IOException {
        MonetaryAmount value = mapper.readValue(resourceStream("price_highFraction.json"), moneyType);

        assertMoneyValues(2.9501, "EUR", value);
    }

    @ParameterizedTest
    @MethodSource("moneyTypes")
    void differentlyOrderedProperties_shouldDeserializeJson(Class<T> moneyType) throws IOException {
        MonetaryAmount value = mapper.readValue(resourceStream("price_reordered.json"), moneyType);

        assertMoneyValues(2.99, "EUR", value);
    }

    @ParameterizedTest
    @MethodSource("moneyTypes")
    void priceWithFormattedProperty_shouldDeserialize(Class<T> moneyType) throws IOException {
        MonetaryAmount value = mapper.readValue(resourceStream("price_formatted.json"), moneyType);

        assertMoneyValues(2.99, "EUR", value);
    }

    @ParameterizedTest
    @MethodSource("moneyTypes")
    void embeddedPrice_shouldDeserialize(Class<T> moneyType) throws IOException {
        MonetaryAmount amount = mapper.readValue(resourceStream("price.json"), moneyType);
        JsonNode cookieNode = mapper.readTree(resourceStream("cookie.json"));
        Cookie cookie = new Cookie(amount);

        Cookie result = mapper.readerForUpdating(cookie).readValue(cookieNode);

        assertNotNull(result);
        assertMoneyValues(2.99, "EUR", result.getPrice());
    }

    @ParameterizedTest
    @MethodSource("moneyTypes")
    void enableFailWithAdditionalProperty_shouldThrowException(Class<T> moneyType) {
        assertThrows(UnrecognizedPropertyException.class,
            () -> mapper.readValue(resourceStream("price_additional.json"), moneyType));
    }

    @ParameterizedTest
    @MethodSource("moneyTypes")
    void disabledFailWithAdditionalProperty_shouldNotThrowException(Class<T> moneyType) {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        assertDoesNotThrow(() -> mapper.readValue(resourceStream("price_additional.json"), moneyType));
    }

    static InputStream resourceStream(String filename) {
        return MonetaryAmountDeserializer.class.getClassLoader().getResourceAsStream(filename);
    }

    private static void assertMoneyValues(double actualAmount, String actualCurrencyCode, MonetaryAmount expected) {
        assertEquals(BigDecimal.valueOf(actualAmount), MonetaryAmountToBigDecimalConverter.INSTANCE.convert(expected));
        assertEquals(actualCurrencyCode, expected.getCurrency().getCurrencyCode());
    }

    static Iterable<Arguments> moneyTypes() {
        return Arrays.asList(
            Arguments.of(MonetaryAmount.class),
            Arguments.of(Money.class),
            Arguments.of(RoundedMoney.class),
            Arguments.of(FastMoney.class)
        );
    }

    @Value
    static class Cookie {
        MonetaryAmount price;
    }
}
