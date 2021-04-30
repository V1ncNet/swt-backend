package de.team7.swt.domain.infrastructure.money;

import de.team7.swt.domain.infrastructure.money.Jsr354Converters.MonetaryAmountToStringConverter;
import de.team7.swt.domain.infrastructure.money.Jsr354Converters.StringToMonetaryAmountConverter;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import java.util.Objects;
import javax.money.MonetaryAmount;

/**
 * A lazy-loaded singleton Hibernate {@link org.hibernate.type.descriptor.java.JavaTypeDescriptor} for marshalling
 * between strings and {@link MonetaryAmount} instances.
 *
 * @author Vincent Nadoll
 * @see MonetaryAmountAttributeConverter
 */
public class MonetaryAmountTypeDescriptor extends AbstractTypeDescriptor<MonetaryAmount> {

    protected MonetaryAmountTypeDescriptor() {
        super(MonetaryAmount.class);
    }

    @Override
    public MonetaryAmount fromString(String string) {
        return Objects.nonNull(string) ? StringToMonetaryAmountConverter.INSTANCE.convert(string) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X unwrap(MonetaryAmount value, Class<X> type, WrapperOptions options) {
        if (null == value) {
            return null;
        }

        if (MonetaryAmount.class.isAssignableFrom(type)) {
            return (X) value;
        } else if (String.class.isAssignableFrom(type)) {
            return (X) MonetaryAmountToStringConverter.INSTANCE.convert(value);
        }

        throw unknownUnwrap(type);
    }

    @Override
    public <X> MonetaryAmount wrap(X value, WrapperOptions options) {
        if (null == value) {
            return null;
        }

        if (value instanceof MonetaryAmount) {
            return (MonetaryAmount) value;
        } else if (value instanceof String) {
            return fromString((String) value);
        }

        throw unknownWrap(value.getClass());
    }

    public static MonetaryAmountTypeDescriptor getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final MonetaryAmountTypeDescriptor INSTANCE = new MonetaryAmountTypeDescriptor();
    }
}
