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

    /**
     * Constructs a new quantity with {@link Metric#UNIT} metric.
     *
     * @return a new quantity instance
     */
    public static Quantity of(long amount) {
        return of(amount, Metric.UNIT);
    }

    /**
     * Constructs a new quantity with {@link Metric#UNIT} metric.
     *
     * @return a new quantity instance
     */
    public static Quantity of(double amount) {
        return of(amount, Metric.UNIT);
    }

    /**
     * Constructs a new quantity.
     *
     * @param metric must not be {@literal null}
     * @return a new quantity instance
     */
    public static Quantity of(long amount, Metric metric) {
        return of(BigDecimal.valueOf(amount), metric);
    }

    /**
     * Constructs a new quantity.
     *
     * @param metric must not be {@literal null}
     * @return a new quantity instance
     */
    public static Quantity of(double amount, Metric metric) {
        return of(BigDecimal.valueOf(amount), metric);
    }

    /**
     * Constructs a new quantity.
     *
     * @param amount must not be {@literal null}
     * @param metric must not be {@literal null}
     * @return a new quantity instance
     */
    public static Quantity of(@NonNull BigDecimal amount, @NonNull Metric metric) {
        return new Quantity(amount, metric);
    }

    /**
     * Determines the compatibility between this and the given metric. {@link #NONE} will always return {@literal
     * true}.
     *
     * @param metric must not be {@literal null}
     * @return {@literal true} if the given metric is compatible with this metric; {@literal false} otherwise.
     */
    public boolean isCompatibleWith(Metric metric) {
        if (this == NONE) {
            return true;
        }

        Assert.notNull(metric, "Metric must not be null");
        return Objects.equals(this.metric, metric);
    }

    /**
     * Adds the given quantity's amount to this amount. All the rules of mathematical addition can also be used here. A
     * {@literal null}-value will be interpreted as 0.
     *
     * @param other may be {@literal null}
     * @return a new quantity instance with added amounts
     * @throws MetricMismatchException in case the metrics didn't match
     */
    public Quantity add(@Nullable Quantity other) {
        return addSub(other, this.amount::add);
    }

    /**
     * Subtract the given quantity's amount from this amount. All the rules of mathematical subtraction can also be used
     * here. A {@literal null}-value will be interpreted as 0.
     *
     * @param other may be {@literal null}
     * @return a new quantity instance with subtracted amounts
     * @throws MetricMismatchException in case the metrics didn't match
     */
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

    /**
     * Scales this quantity's amount with with the given multiplier.
     *
     * @return a new scaled quantity instance
     */
    public Quantity times(int multiplier) {
        return times((long) multiplier);
    }

    /**
     * Scales this quantity's amount with with the given multiplier.
     *
     * @return a new scaled quantity instance
     */
    public Quantity times(long multiplier) {
        BigDecimal product = amount.multiply(BigDecimal.valueOf(multiplier));
        return new Quantity(product, metric);
    }

    /**
     * Returns whether this and the given quantity are equal.
     *
     * @return {@literal true} if amount and metric are considered equal; {@literal false} otherwise.
     */
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

    /**
     * Returns whether this amount is less than the other's amount.
     *
     * @param other must not be {@literal null}
     * @return {@literal true} if the given amount is not less; {@literal false} otherwise
     * @throws MetricMismatchException in case the metrics didn't match
     */
    public boolean isLessThan(Quantity other) {
        return compareTo(other) < 0;
    }

    /**
     * Returns whether this amount is grater than the other's amount.
     *
     * @param other must not be {@literal null}
     * @return {@literal true} if the given amount is not greater; {@literal false} otherwise
     * @throws MetricMismatchException in case the metrics didn't match
     */
    public boolean isGreaterThan(Quantity other) {
        return compareTo(other) > 0;
    }

    /**
     * Returns whether this amount is grater or equal than the other's amount.
     *
     * @param other must not be {@literal null}
     * @return {@literal true} if the given amount is not greater than or equal; {@literal false} otherwise
     * @throws MetricMismatchException in case the metrics didn't match
     */
    public boolean isGreaterThanOrEqualTo(Quantity other) {
        return compareTo(other) >= 0;
    }

    /**
     * Compares this and the other's amount.
     *
     * @param other must not be {@literal null}
     * @return -1, 0, or 1 as this amount is numerically less than, equal to, or greater than the other
     * @throws MetricMismatchException in case the metrics didn't match
     */
    @Override
    public int compareTo(Quantity other) {
        assertCompatibility(other);
        return Objects.compare(this.amount, other.amount, BigDecimal::compareTo);
    }

    /**
     * Returns whether the amount is negative.
     *
     * @return {@literal true} if the amount is less than 0; {@literal false} otherwise
     */
    @Transient
    public boolean isNegative() {
        return Objects.compare(this.amount, BigDecimal.ZERO, BigDecimal::compareTo) < 0;
    }

    /**
     * Returns whether this amount is 0 or negative.
     *
     * @return {@literal true} if the amount is negative or 0; {@literal false} otherwise
     */
    @Transient
    public boolean isZeroOrNegative() {
        return !isGreaterThan(toZero());
    }

    /**
     * Constructs a new quantity with the amount of zero but with the same metric as the originating quantity.
     *
     * @return a new quantity instance
     */
    public Quantity toZero() {
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

    /**
     * Pretty-prints the current quantity. Use {@link QuantityFormatter#print(Quantity, java.util.Locale)} to print a
     * localized quantity string.
     */
    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(amount.scale());
        return format.format(amount).concat(metric.getAbbreviation());
    }
}
