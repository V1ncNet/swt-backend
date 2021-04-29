package de.team7.swt.domain.infrastructure.quantity;

import de.team7.swt.domain.quantity.Quantity;
import de.team7.swt.domain.quantity.QuantityFormatter;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

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
@ConfigurationPropertiesBinding
public class StringToQuantityConverter implements Converter<String, Quantity> {

    private static final QuantityFormatter FORMATTER = QuantityFormatter.getInstance();

    @Override
    public Quantity convert(String source) {
        try {
            return FORMATTER.parse(source, Locale.US);
        } catch (ParseException e) {
            TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
            TypeDescriptor targetType = TypeDescriptor.valueOf(Quantity.class);
            throw new ConversionFailedException(sourceType, targetType, source, e);
        }
    }
}
