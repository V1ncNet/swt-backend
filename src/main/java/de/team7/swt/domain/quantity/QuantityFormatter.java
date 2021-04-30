package de.team7.swt.domain.quantity;

import org.springframework.format.Formatter;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Spring dedicated {@link Formatter} to parse and print {@link Quantity} instances. The formatter is capable of
 * parsing common localized and signed quantity strings with typical measurement unit (abbreviations). It doesn't matter
 * if the metric is separated by the quantity's amount or not. In addition, the number 0 in front of the decimal/comma
 * point can be omitted.
 *
 * @author Vincent Nadoll
 * @see NumberStyleFormatter
 */
@Component
public class QuantityFormatter implements Formatter<Quantity> {

    private static final Pattern QUANTITY_PATTERN = Pattern.compile("([+-]?\\d*[.,]?\\d*)(\\s*\\w*)");
    private static final NumberStyleFormatter NUMBER_FORMATTER = new NumberStyleFormatter();

    @Override
    public Quantity parse(@Nullable String text, @Nullable Locale locale) throws ParseException {
        if (!StringUtils.hasText(text) || null == locale) {
            return Quantity.NONE;
        }

        Matcher matcher = QUANTITY_PATTERN.matcher(text.trim());
        if (!matcher.matches()) {
            throw new ParseException(text, 0);
        }

        Metric metric = parseMetric(matcher);
        Number number = NUMBER_FORMATTER.parse(matcher.group(1), locale);
        return Quantity.of(number.doubleValue(), metric);
    }

    private static Metric parseMetric(Matcher matcher) throws ParseException {
        String source = matcher.group(2);
        try {
            return StringUtils.hasText(source) ? Metric.from(source) : Metric.UNIT;
        } catch (IllegalArgumentException e) {
            throw new ParseException(source, matcher.start(2));
        }
    }

    @Override
    public String print(Quantity quantity, Locale locale) {
        Assert.notNull(quantity, "Quantity must not be null");
        Assert.notNull(locale, "Locale must not be null");
        return String.format("%s%s",
            NUMBER_FORMATTER.print(quantity.getAmount(), locale),
            quantity.getMetric().getAbbreviation());
    }
}
