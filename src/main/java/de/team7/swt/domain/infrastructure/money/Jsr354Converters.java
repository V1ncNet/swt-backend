package de.team7.swt.domain.infrastructure.money;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.format.number.money.MonetaryAmountFormatter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Locale;
import javax.money.MonetaryAmount;

/**
 * Collection of JSR-354 converters to be used by the JPA and Hibernate ORM implementation.
 *
 * @author Vincent Nadoll
 * @see <a href="http://download.oracle.com/otn-pub/jcp/money_currency-1_0-fr-eval-spec/JavaMoney_Specification_1.0-final.pdf">JavaMoney_Specification</a>
 */
public class Jsr354Converters {

    /**
     * {@link Converter} for converting money compatible strings to a new {@link MonetaryAmount} instance.
     *
     * @author Vincent Nadoll
     * @see MonetaryAmountFormatter
     */
    @ReadingConverter
    public enum StringToMonetaryAmountConverter implements Converter<String, MonetaryAmount> {
        INSTANCE;

        /**
         * Converts the given US-localized string to a new {@link MonetaryAmount} instance.
         *
         * @param source must not be {@literal null}
         * @return the converted object; never {@literal null}
         * @throws IllegalArgumentException                  in case the argument is {@literal null}
         * @throws javax.money.format.MonetaryParseException in case the argument could not be parsed
         */
        @NonNull
        @Override
        public MonetaryAmount convert(String source) {
            Assert.hasText(source, "Source must not be empty");
            MonetaryAmountFormatter formatter = new MonetaryAmountFormatter();
            return formatter.parse(source, Locale.US);
        }
    }

    /**
     * {@link Converter} for converting a {@link MonetaryAmount} instance to a string.
     *
     * @author Vincent Nadoll
     * @see MonetaryAmountFormatter
     */
    @WritingConverter
    public enum MonetaryAmountToStringConverter implements Converter<MonetaryAmount, String> {
        INSTANCE;

        /**
         * Converts the given {@link MonetaryAmount} to a US-localized string.
         *
         * @param source must not be {@literal null}
         * @return the converted object; never {@literal null}
         * @throws IllegalArgumentException in case the argument is {@literal null}
         */
        @NonNull
        @Override
        public String convert(MonetaryAmount source) {
            Assert.notNull(source, "Source must not be empty");
            MonetaryAmountFormatter formatter = new MonetaryAmountFormatter();
            return formatter.print(source, Locale.US);
        }
    }
}
