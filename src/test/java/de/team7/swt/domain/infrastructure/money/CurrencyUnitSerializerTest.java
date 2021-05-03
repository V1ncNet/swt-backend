package de.team7.swt.domain.infrastructure.money;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javamoney.moneta.CurrencyUnitBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.CurrencyUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Vincent Nadoll
 */
class CurrencyUnitSerializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Test
    void currencyCode_shouldBeSerialized() throws JsonProcessingException {
        CurrencyUnit currency = CurrencyUnitBuilder.of("EUR", "default").build();

        String value = mapper.writeValueAsString(currency);

        assertNotNull(value);
        assertEquals("\"EUR\"", value);
    }
}
