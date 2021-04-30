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

    @ReadingConverter
    public enum StringToMonetaryAmountConverter implements Converter<String, MonetaryAmount> {
        INSTANCE;

        @NonNull
        @Override
        public MonetaryAmount convert(String source) {
            Assert.hasText(source, "Source must not be empty");
            MonetaryAmountFormatter formatter = new MonetaryAmountFormatter();
            return formatter.parse(source, Locale.US);
        }
    }

    @WritingConverter
    public enum MonetaryAmountToStringConverter implements Converter<MonetaryAmount, String> {
        INSTANCE;

        @NonNull
        @Override
        public String convert(MonetaryAmount source) {
            Assert.notNull(source, "Source must not be empty");
            MonetaryAmountFormatter formatter = new MonetaryAmountFormatter();
            return formatter.print(source, Locale.US);
        }
    }
}
