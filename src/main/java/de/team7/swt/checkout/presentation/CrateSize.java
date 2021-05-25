package de.team7.swt.checkout.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

/**
 * Enumeration of common beer crate sizes.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
enum CrateSize {
    SIX(6),
    ELEVEN(11),
    TWENTY(20),
    ;

    final int amount;

    /**
     * Multiplies the given argument with this create sizes amount.
     *
     * @param multiplier must be negative or zero
     * @return product of this crate site and the given multiplier
     */
    public long times(int multiplier) {
        Assert.isTrue(multiplier > 0, "Multiplier must be greater than zero");
        return (long) this.amount * multiplier;
    }

    /**
     * Multiplies the given argument with this create sizes amount.
     *
     * @param multiplier must be negative or zero
     * @return product of this crate site and the given multiplier
     */
    public long times(long multiplier) {
        Assert.isTrue(multiplier > 0, "Multiplier must be greater than zero");
        return this.amount * multiplier;
    }
}
