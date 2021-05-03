package de.team7.swt.domain.infrastructure.money;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javamoney.moneta.CurrencyUnitBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.CurrencyUnit;
import javax.money.UnknownCurrencyException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
class CurrencyUnitDeserializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Test
    void invalidCurrencyCode_shouldThrowException() {
        assertThrows(UnknownCurrencyException.class, () -> mapper.readValue("\"FOO\"", CurrencyUnit.class));
    }

    @Test
    void validCurrencyCode_shouldBeDeserialized() throws JsonProcessingException {
        CurrencyUnit value = mapper.readValue("\"EUR\"", CurrencyUnit.class);

        assertNotNull(value);
        assertEquals(CurrencyUnitBuilder.of("EUR", "default").build(), value);
    }
}
