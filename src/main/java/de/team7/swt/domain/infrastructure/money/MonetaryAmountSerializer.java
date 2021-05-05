package de.team7.swt.domain.infrastructure.money;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.format.number.money.MonetaryAmountFormatter;

import java.io.IOException;
import java.math.BigDecimal;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import static de.team7.swt.domain.infrastructure.money.FieldNames.AMOUNT;
import static de.team7.swt.domain.infrastructure.money.FieldNames.CURRENCY;
import static de.team7.swt.domain.infrastructure.money.FieldNames.FORMATTED;

/**
 * {@link com.fasterxml.jackson.databind.JsonSerializer Serializer} for writing a {@link MonetaryAmount} instance to the
 * {@link JsonGenerator}.
 *
 * @author Vincent Nadoll
 */
public class MonetaryAmountSerializer extends StdSerializer<MonetaryAmount> {

    private final MonetaryAmountFormatter formatter = new MonetaryAmountFormatter();

    protected MonetaryAmountSerializer() {
        super(MonetaryAmount.class);
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper wrapper, JavaType typeHint)
        throws JsonMappingException {
        JsonObjectFormatVisitor visitor = wrapper.expectObjectFormat(typeHint);
        if (null == visitor) {
            return;
        }

        SerializerProvider provider = visitor.getProvider();

        visitor.property(AMOUNT,
            provider.findValueSerializer(BigDecimal.class),
            provider.constructType(BigDecimal.class));

        visitor.property(CURRENCY,
            provider.findValueSerializer(CurrencyUnit.class),
            provider.constructType(CurrencyUnit.class));

        visitor.optionalProperty(FORMATTED,
            provider.findValueSerializer(String.class),
            provider.constructType(String.class));
    }

    @Override
    public void serialize(MonetaryAmount value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField(AMOUNT, Jsr354Converters.MonetaryAmountToBigDecimalConverter.INSTANCE.convert(value));
        provider.defaultSerializeField(CURRENCY, value.getCurrency(), gen);
        gen.writeStringField(FORMATTED, formatter.print(value, provider.getLocale()));
        gen.writeEndObject();
    }
}
