package de.team7.swt.domain.infrastructure.money;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;

import static de.team7.swt.domain.infrastructure.money.FieldNames.AMOUNT;
import static de.team7.swt.domain.infrastructure.money.FieldNames.CURRENCY;
import static de.team7.swt.domain.infrastructure.money.FieldNames.FORMATTED;

/**
 * {@link JsonDeserializer Deserializer} for converting a JSON string-property to a new {@link MonetaryAmount}
 * instance.
 *
 * @param <T> the type of {@link MonetaryAmount}
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class MonetaryAmountDeserializer<T extends MonetaryAmount> extends JsonDeserializer<T> {

    private static final String NOT_NULL_TEMPLATE = "Missing property [%s]";

    private final MonetaryAmountFactory<T> factory;

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        BigDecimal amount = null;
        CurrencyUnit currency = null;

        while (p.nextToken() != JsonToken.END_OBJECT) {
            String field = p.getCurrentName();
            p.nextToken();

            if (AMOUNT.equals(field)) {
                amount = ctxt.readValue(p, BigDecimal.class);
            } else if (CURRENCY.equals(field)) {
                currency = ctxt.readValue(p, CurrencyUnit.class);
            } else if (FORMATTED.equals(field)) {
                continue;
            } else if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
                throw UnrecognizedPropertyException.from(p, MonetaryAmount.class, field, FieldNames.mandatory());
            } else {
                p.skipChildren();
            }
        }

        assertNotNull(amount, p, String.format(NOT_NULL_TEMPLATE, AMOUNT));
        assertNotNull(currency, p, String.format(NOT_NULL_TEMPLATE, AMOUNT));

        factory.setNumber(amount);
        factory.setCurrency(currency);
        return factory.create();
    }

    private static void assertNotNull(Object object, JsonParser parser, String message) throws JsonParseException {
        if (null == object) {
            throw new JsonParseException(parser, message);
        }
    }
}
