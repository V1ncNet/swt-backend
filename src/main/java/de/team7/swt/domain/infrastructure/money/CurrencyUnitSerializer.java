package de.team7.swt.domain.infrastructure.money;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import javax.money.CurrencyUnit;

/**
 * {@link com.fasterxml.jackson.databind.JsonSerializer Serializer} for writing a {@link CurrencyUnit} instance to the
 * {@link JsonGenerator}.
 *
 * @author Vincent Nadoll
 */
public class CurrencyUnitSerializer extends StdSerializer<CurrencyUnit> {

    protected CurrencyUnitSerializer() {
        super(CurrencyUnit.class);
    }

    @Override
    public void serialize(CurrencyUnit value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getCurrencyCode());
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
        throws JsonMappingException {
        visitor.expectStringFormat(typeHint);
    }
}
