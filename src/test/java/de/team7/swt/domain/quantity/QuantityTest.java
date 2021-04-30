package de.team7.swt.domain.quantity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vincent Nadoll
 */
class QuantityTest {

    @Test
    void initializeNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Quantity.of(null, Metric.UNIT));
        assertThrows(IllegalArgumentException.class, () -> Quantity.of(0, null));
        assertThrows(IllegalArgumentException.class, () -> Quantity.of(.0, null));
        assertThrows(IllegalArgumentException.class, () -> Quantity.of(null, null));
    }

    @Test
    void noneQuantity_shouldHaveZeroAmountAndUnitMetric() {
        Quantity none = Quantity.NONE;

        assertEquals(BigDecimal.ZERO, none.getAmount());
        assertEquals(Metric.UNIT, none.getMetric());
    }

    @Test
    void instantiation_shouldDefaultToUnitMetric() {
        Quantity one = Quantity.of(1);

        assertEquals(Metric.UNIT, one.getMetric());
    }

    @Test
    void amountInstantiation_shouldBeCompatibleToUnitMetric() {
        Quantity one = Quantity.of(1);

        assertTrue(one.isCompatibleWith(Metric.UNIT));
        assertFalse(one.isCompatibleWith(Metric.LITER));
    }

    @Test
    void noneQuantity_shouldBeCompatibleWithAnyMetric() {
        Quantity none = Quantity.NONE;

        assertTrue(none.isCompatibleWith(null));
        Arrays.stream(Metric.values())
            .map(none::isCompatibleWith)
            .forEach(Assertions::assertTrue);
    }

    @Test
    void nonZeroQuantities_shouldShouldBeCompatibleWithMatchingMetric() {
        assertTrue(Quantity.of(1, Metric.LITER).isCompatibleWith(Metric.LITER));
        assertFalse(Quantity.of(1, Metric.LITER).isCompatibleWith(Metric.UNIT));
        assertFalse(Quantity.of(1, Metric.UNIT).isCompatibleWith(Metric.LITER));
    }

    @Test
    void addLiterToUnit_shouldThrowException() {
        assertThrows(MetricMismatchException.class,
            () -> Quantity.of(1, Metric.UNIT).add(Quantity.of(1, Metric.LITER)));
    }

    @Test
    void addNull_shouldAddZero() {
        assertEquals(Quantity.NONE, Quantity.NONE.add(null));
        assertEquals(Quantity.of(1), Quantity.of(1).add(null));
    }

    @Test
    void add_shouldReflectAddMathOperation() {
        Quantity zero = Quantity.of(0);
        Quantity one = Quantity.of(1);
        Quantity oneAndAHalf = Quantity.of(1.5);

        assertEquals(Quantity.of(2), one.add(one));
        assertEquals(Quantity.of(2.5), oneAndAHalf.add(one));
        assertEquals(one, zero.add(one));
        assertEquals(one, one.add(zero));
        assertEquals(zero, zero.add(zero));
        assertEquals(one, one.add(Quantity.NONE));
        assertNotEquals(one, oneAndAHalf.add(zero));
    }

    @Test
    void subtractLiterFromUnit_shouldThrowException() {
        assertThrows(MetricMismatchException.class,
            () -> Quantity.of(1, Metric.UNIT).subtract(Quantity.of(1, Metric.LITER)));
    }

    @Test
    void subtractNull_shouldSubtractZero() {
        assertEquals(Quantity.NONE, Quantity.NONE.subtract(null));
        assertEquals(Quantity.of(1), Quantity.of(1).subtract(null));
    }

    @Test
    void subtract_shouldReflectSubtractMathOperation() {
        Quantity zero = Quantity.of(0);
        Quantity one = Quantity.of(1);
        Quantity oneAndAHalf = Quantity.of(1.5);

        assertEquals(zero, one.subtract(one));
        assertEquals(Quantity.of(0.5), oneAndAHalf.subtract(one));
        assertEquals(Quantity.of(-1), zero.subtract(one));
        assertEquals(one, one.subtract(zero));
        assertEquals(zero, zero.subtract(zero));
        assertEquals(one, one.subtract(Quantity.NONE));
        assertNotEquals(one, oneAndAHalf.subtract(zero));
    }

    @Test
    void multiplyInteger_shouldReflectMathOperation() {
        Quantity zero = Quantity.of(0);
        Quantity one = Quantity.of(1);
        Quantity oneAndAHalf = Quantity.of(1.5);

        assertEquals(one, one.times(1));
        assertEquals(Quantity.of(3), oneAndAHalf.times(2L));
        assertEquals(zero, zero.times(1L));
        assertEquals(zero, one.times(0));
        assertEquals(zero, zero.times(0));
        assertNotEquals(one, oneAndAHalf.times(1));
    }

    @Test
    void equals_shouldCompareProperties() {
        Quantity zeroUnits = Quantity.of(0);
        Quantity otherZeroUnits = Quantity.of(0);
        Quantity zeroLiter = Quantity.of(0, Metric.LITER);

        assertEquals(zeroUnits, zeroUnits);
        assertEquals(zeroUnits, otherZeroUnits);
        assertNotEquals(zeroLiter, otherZeroUnits);
        assertNotEquals(zeroUnits, new Object());
        assertNotEquals(zeroUnits, null);
        assertNotEquals(null, zeroUnits);
    }

    @Test
    void isLessThan_shouldCompareAmounts() {
        Quantity zero = Quantity.of(0);
        Quantity one = Quantity.of(1);

        assertTrue(zero.isLessThan(one));
        assertFalse(zero.isLessThan(zero));
        assertFalse(one.isLessThan(zero));
    }

    @Test
    void isGreaterThan_shouldCompareAmounts() {
        Quantity zero = Quantity.of(0);
        Quantity one = Quantity.of(1);

        assertTrue(one.isGreaterThan(zero));
        assertFalse(zero.isGreaterThan(zero));
        assertFalse(zero.isGreaterThan(one));
    }

    @Test
    void isGreaterThanOrEqualTo_shouldCompareAmounts() {
        Quantity zero = Quantity.of(0);
        Quantity one = Quantity.of(1);

        assertTrue(one.isGreaterThanOrEqualTo(zero));
        assertTrue(zero.isGreaterThanOrEqualTo(zero));
        assertFalse(zero.isGreaterThanOrEqualTo(one));
    }

    @Test
    void isNegative_shouldEvaluateAmount() {
        assertFalse(Quantity.of(1).isNegative());
        assertFalse(Quantity.of(0).isNegative());
        assertTrue(Quantity.of(-1).isNegative());
    }

    @Test
    void isZeroOrNegative_shouldEvaluateAmount() {
        assertFalse(Quantity.of(1).isZeroOrNegative());
        assertTrue(Quantity.of(0).isZeroOrNegative());
        assertTrue(Quantity.of(-1).isZeroOrNegative());
    }

    @Test
    void zero_shouldReturnNewQuantity() {
        Quantity zero = Quantity.of(0);
        Quantity one = Quantity.of(1);

        assertEquals(zero, one.toZero());
        assertNotEquals(one, one.toZero());
    }

    @Test
    void setAmountToAbsoluteMetric_shouldSanitizeAmount() {
        Quantity zero = Quantity.of(0);

        zero.setAmount(BigDecimal.valueOf(1.500));
        BigDecimal amount = zero.getAmount();

        assertNotNull(amount);
        assertEquals(BigDecimal.ONE, amount);
    }

    @Test
    void setAmountToRationalMetric_shouldNotSanitize() {
        Quantity one = Quantity.of(1, Metric.LITER);

        one.setAmount(BigDecimal.valueOf(1.500));
        BigDecimal amount = one.getAmount();

        assertNotNull(amount);
        assertEquals(BigDecimal.valueOf(1.5), amount);
    }

    @Test
    void setAbsoluteMetric_shouldSanitizeAmount() {
        Quantity oneAndAHalf = Quantity.of(1.5, Metric.LITER);

        oneAndAHalf.setMetric(Metric.UNIT);
        BigDecimal amount = oneAndAHalf.getAmount();

        assertNotNull(amount);
        assertEquals(BigDecimal.ONE, amount);
    }
}

