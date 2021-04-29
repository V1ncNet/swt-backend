package de.team7.swt.domain.quantity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.function.UnaryOperator;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * A value object encapsulating a quantitative amount and its {@link Metric}.
 *
 * @author Vincent Nadoll
 */
@Embeddable
@Access(AccessType.PROPERTY)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quantity implements Comparable<Quantity> {

    public static final Quantity NONE = Quantity.of(0);

    @NonNull
    @Getter(onMethod_ = @Column(name = "quantity_amount"))
    private BigDecimal amount;

    @NonNull
    @Getter(onMethod_ = @Column(name = "quantity_metric"))
    private Metric metric;

    public static Quantity of(long amount) {
        return of(amount, Metric.UNIT);
    }

    public static Quantity of(double amount) {
        return of(amount, Metric.UNIT);
    }

    public static Quantity of(long amount, Metric metric) {
        return of(BigDecimal.valueOf(amount), metric);
    }

    public static Quantity of(double amount, Metric metric) {
        return of(BigDecimal.valueOf(amount), metric);
    }

    public static Quantity of(@NonNull BigDecimal amount, @NonNull Metric metric) {
        return new Quantity(amount, metric);
    }

    public boolean isCompatibleWith(Metric metric) {
        if (this == NONE) {
            return true;
        }

        Assert.notNull(metric, "Metric must not be null");
        return Objects.equals(this.metric, metric);
    }

    public Quantity add(@Nullable Quantity other) {
        return addSub(other, this.amount::add);
    }

    public Quantity subtract(@Nullable Quantity other) {
        return addSub(other, this.amount::subtract);
    }

    private Quantity addSub(@Nullable Quantity other, UnaryOperator<BigDecimal> operation) {
        if (null == other) {
            return Quantity.of(amount, metric);
        } else if (other == NONE) {
            return this;
        }

        assertCompatibility(other);
        return new Quantity(operation.apply(other.amount), this.metric);
    }

    public Quantity times(int multiplier) {
        return times((long) multiplier);
    }

    public Quantity times(long multiplier) {
        BigDecimal product = amount.multiply(BigDecimal.valueOf(multiplier));
        return new Quantity(product, metric);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Quantity)) {
            return false;
        } else {
            Quantity other = (Quantity) obj;
            return Objects.equals(this.metric, other.metric)
                && 0 == Objects.compare(this.amount, other.amount, BigDecimal::compareTo);
        }
    }

    public boolean isLessThan(Quantity other) {
        return compareTo(other) < 0;
    }

    public boolean isGreaterThan(Quantity other) {
        return compareTo(other) > 0;
    }

    public boolean isGreaterThanOrEqualTo(Quantity other) {
        return compareTo(other) >= 0;
    }

    @Override
    public int compareTo(Quantity other) {
        assertCompatibility(other);
        return Objects.compare(this.amount, other.amount, BigDecimal::compareTo);
    }

    @Transient
    public boolean isNegative() {
        return Objects.compare(this.amount, BigDecimal.ZERO, BigDecimal::compareTo) < 0;
    }

    @Transient
    public boolean isZeroOrNegative() {
        return !isGreaterThan(zero());
    }

    public Quantity zero() {
        return Quantity.of(0, metric);
    }

    private void assertCompatibility(Quantity quantity) {
        Assert.notNull(quantity, "Quantity must not be null");
        if (Objects.equals(this, NONE) || Objects.equals(quantity, NONE)) {
            return;
        }

        if (!isCompatibleWith(quantity.metric)) {
            throw new MetricMismatchException(
                String.format("Quantity [%s] is incompatible to quantity [%s]", this, quantity),
                metric, quantity.metric
            );
        }
    }

    void setAmount(BigDecimal amount) {
        this.amount = amount;
        sanitize(amount, metric);
    }

    void setMetric(Metric metric) {
        this.metric = metric;
        sanitize(amount, metric);
    }

    private void sanitize(BigDecimal amount, Metric metric) {
        if (null != amount && Objects.equals(Metric.UNIT, metric)) {
            this.amount = BigDecimal.valueOf(amount.longValue());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, metric);
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(amount.scale());
        return format.format(amount).concat(metric.getAbbreviation());
    }
}
