package de.team7.swt.domain.infrastructure.money;

import de.team7.swt.domain.infrastructure.money.Jsr354Converters.MonetaryAmountToStringConverter;
import de.team7.swt.domain.infrastructure.money.Jsr354Converters.StringToMonetaryAmountConverter;

import java.util.Objects;
import javax.money.MonetaryAmount;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * A JPA {@link AttributeConverter} for serializing a {@link MonetaryAmount} instance into a string column type and vice
 * versa.
 *
 * @author Vincent Nadoll
 * @see javax.persistence.Convert
 * @see Jsr354Converters
 */
@Converter(autoApply = true)
public class MonetaryAmountAttributeConverter implements AttributeConverter<MonetaryAmount, String> {

    @Override
    public String convertToDatabaseColumn(MonetaryAmount amount) {
        return null == amount ? null : MonetaryAmountToStringConverter.INSTANCE.convert(amount);
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(String source) {
        return Objects.nonNull(source) ? StringToMonetaryAmountConverter.INSTANCE.convert(source) : null;
    }
}
