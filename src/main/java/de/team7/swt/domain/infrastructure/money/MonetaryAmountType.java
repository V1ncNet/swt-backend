package de.team7.swt.domain.infrastructure.money;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import javax.money.MonetaryAmount;

/**
 * A lazy-loaded singleton-capable Hibernate {@link org.hibernate.type.BasicType} derivate for the {@link
 * MonetaryAmount} type.
 *
 * @author Vincent Nadoll
 * @see org.hibernate.annotations.Type
 * @see MonetaryAmountTypeDescriptor
 * @see de.team7.swt.domain.infrastructure.GlobalSessionFactoryBuilderFactory
 */
public class MonetaryAmountType extends AbstractSingleColumnStandardBasicType<MonetaryAmount> {

    public MonetaryAmountType() {
        super(VarcharTypeDescriptor.INSTANCE, MonetaryAmountTypeDescriptor.getInstance());
    }

    @Override
    public String getName() {
        return "monetaryAmount";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    public static MonetaryAmountType getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final MonetaryAmountType INSTANCE = new MonetaryAmountType();
    }
}
