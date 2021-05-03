package de.team7.swt.domain.infrastructure.money;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import javax.money.CurrencyUnit;
import javax.money.Monetary;

/**
 * {@link JsonDeserializer Deserializer} for converting a JSON string-property to a new {@link CurrencyUnit} instance.
 *
 * @author Vincent Nadoll
 */
public class CurrencyUnitDeserializer extends JsonDeserializer<CurrencyUnit> {

    @Override
    public CurrencyUnit deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String currencyCode = p.getValueAsString();
        return Monetary.getCurrency(currencyCode);
    }
}
