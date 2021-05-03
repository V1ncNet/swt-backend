package de.team7.swt.domain.infrastructure.money;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.format.number.money.MonetaryAmountFormatter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import javax.money.MonetaryAmount;
import javax.money.format.MonetaryParseException;

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
         * @return the converted object or {@literal null} in case the argument is an empty string
         * @throws IllegalArgumentException in case the argument could not be parsed
         */
        @Override
        @Nullable
        public MonetaryAmount convert(String source) {
            Assert.notNull(source, "Source must not be null");
            return (StringUtils.hasText(source) ? convertNonNull(source.trim()) : null);
        }

        private MonetaryAmount convertNonNull(String source) {
            try {
                MonetaryAmountFormatter formatter = new MonetaryAmountFormatter();
                return formatter.parse(source, Locale.US);
            } catch (UnsupportedOperationException | MonetaryParseException e) {
                throw new IllegalArgumentException(e);
            }
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
         * @throws IllegalArgumentException in case the argument could not be printed
         */
        @NonNull
        @Override
        public String convert(MonetaryAmount source) {
            try {
                Assert.notNull(source, "Source must not be empty");
                MonetaryAmountFormatter formatter = new MonetaryAmountFormatter();
                return formatter.print(source, Locale.US);
            } catch (UnsupportedOperationException | IllegalStateException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    /**
     * {@link Converter} for converting a {@link MonetaryAmount} instance to a {@link BigDecimal} instance.
     *
     * @author Vincent Nadoll
     */
    @WritingConverter
    public enum MonetaryAmountToBigDecimalConverter implements Converter<MonetaryAmount, BigDecimal> {
        INSTANCE;

        /**
         * Extracts the decimal amount of the given argument.
         *
         * @param source must not be {@literal null}
         * @return the extracted number; never {@literal null}
         * @throws IllegalArgumentException in case the argument is {@literal null}
         */
        @NonNull
        @Override
        public BigDecimal convert(MonetaryAmount source) {
            Assert.notNull(source, "Source must not be null");
            BigDecimal decimal = source.getNumber().numberValueExact(BigDecimal.class);
            int defaultFractionDigits = source.getCurrency().getDefaultFractionDigits();
            int scale = Math.max(decimal.scale(), defaultFractionDigits);
            return decimal.setScale(scale, RoundingMode.UNNECESSARY);
        }
    }
}
