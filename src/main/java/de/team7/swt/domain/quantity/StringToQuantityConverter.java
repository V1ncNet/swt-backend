package de.team7.swt.domain.quantity;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.Locale;

/**
 * A dedicated Spring {@link Converter} implementation for converting a String to {@link Quantity} instances. This
 * converter is especially useful for conversions in Spring Data JPA and Spring Web.
 *
 * @author Vincent Nadoll
 * @see QuantityFormatter
 */
@Component
@RequiredArgsConstructor
@ConfigurationPropertiesBinding
public class StringToQuantityConverter implements Converter<String, Quantity> {

    private final QuantityFormatter quantityFormatter;

    @Override
    public Quantity convert(String source) {
        try {
            return quantityFormatter.parse(source, Locale.US);
        } catch (ParseException e) {
            TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
            TypeDescriptor targetType = TypeDescriptor.valueOf(Quantity.class);
            throw new IllegalArgumentException(String.format("Failed to convert from type [%s] to type [%s] for value "
                + "'%s'", sourceType, targetType, ObjectUtils.nullSafeToString(source)), e);
        }
    }
}
