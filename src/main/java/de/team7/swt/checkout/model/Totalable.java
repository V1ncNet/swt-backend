package de.team7.swt.checkout.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.util.Streamable;

import javax.money.MonetaryAmount;

/**
 * An {@link Iterable} stream supplier of {@link Priced} implementations for easy price calculation.
 *
 * @author Vincent Nadoll
 */
public interface Totalable<T extends Priced> extends Streamable<T> {

    /**
     * Returns the total of all {@link Priced} elements.
     *
     * @return summed prices
     */
    default MonetaryAmount getTotal() {
        return Priced.sum(this);
    }

    @JsonIgnore
    @Override
    default boolean isEmpty() {
        return Streamable.super.isEmpty();
    }
}
