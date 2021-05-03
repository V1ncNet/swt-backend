package de.team7.swt.domain.infrastructure.money;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.RoundedMoney;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Locale;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * @author Vincent Nadoll
 */
class MonetaryAmountSerializerTest {

    private static final CurrencyUnit EURO = Monetary.getCurrency("EUR");

    private ObjectMapper mapper;
    private ObjectWriter writer;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        writer = mapper.writer().with(Locale.US);
    }


    @ParameterizedTest
    @MethodSource("amounts")
    void configuredUsLocale_shouldSerialize(MonetaryAmount amount) throws JsonProcessingException {
        String value = writer.writeValueAsString(amount);

        assertNotNull(value);
        assertEquals("{\"amount\":2.99,\"currency\":\"EUR\",\"formatted\":\"EUR2.99\"}", value);
    }

    @ParameterizedTest
    @MethodSource("amounts")
    void configuredGermanLocale_shouldSerialize(MonetaryAmount amount) throws JsonProcessingException {
        ObjectWriter writer = mapper.writer().with(Locale.GERMAN);

        String value = writer.writeValueAsString(amount);

        assertNotNull(value);
        assertEquals("{\"amount\":2.99,\"currency\":\"EUR\",\"formatted\":\"2,99 EUR\"}", value);
    }

    @ParameterizedTest
    @MethodSource("hundreds")
    void amountAsDecimalWithDefaultFractionDigits_shouldSerialize(MonetaryAmount amount) throws JsonProcessingException {
        String value = writer.writeValueAsString(amount);

        assertNotNull(value);
        assertEquals("{\"amount\":100.00,\"currency\":\"EUR\",\"formatted\":\"EUR100.00\"}", value);
    }

    @ParameterizedTest
    @MethodSource("fractions")
    void amountAsDecimalWithMoreFractionDigits_shouldSerialize(MonetaryAmount amount) throws JsonProcessingException {
        String value = writer.writeValueAsString(amount);

        assertNotNull(value);
        assertEquals("{\"amount\":0.9501,\"currency\":\"EUR\",\"formatted\":\"EUR0.95\"}", value);
    }

    @Test
    void acceptJsonFormatVisitor_shouldHandleNullValue() {
        JsonFormatVisitorWrapper wrapper = mock(JsonFormatVisitorWrapper.class);
        MonetaryAmountSerializer serializer = new MonetaryAmountSerializer();

        assertDoesNotThrow(() -> serializer.acceptJsonFormatVisitor(wrapper, SimpleType.constructUnsafe(MonetaryAmount.class)));
    }

    static Iterable<MonetaryAmount> amounts() {
        return Arrays.asList(
            Money.of(2.99, EURO),
            RoundedMoney.of(2.99, EURO),
            FastMoney.of(2.99, EURO)
        );
    }

    static Iterable<MonetaryAmount> hundreds() {
        return Arrays.asList(
            Money.of(100, EURO),
            RoundedMoney.of(100, EURO),
            FastMoney.of(100, EURO)
        );
    }

    static Iterable<MonetaryAmount> fractions() {
        return Arrays.asList(
            Money.of(0.9501, EURO),
            RoundedMoney.of(0.9501, EURO),
            FastMoney.of(0.9501, EURO)
        );
    }
}
