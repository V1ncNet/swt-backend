package de.team7.swt.checkout.model;

import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;

/**
 * Interface indicating a priced item.
 *
 * @author Vincent Nadoll
 */
interface Priced {

    /**
     * Returns the implementation's price.
     *
     * @return the price of the item
     */
    MonetaryAmount getPrice();

    /**
     * Sums up the prices of the given collection of priced elements.
     *
     * @param priced must no be {@literal null}
     * @return summed up prices
     */
    static MonetaryAmount sum(Iterable<? extends Priced> priced) {
        Assert.notNull(priced, "Iterable must not be empty");

        return Streamable.of(priced)
            .stream()
            .map(Priced::getPrice)
            .reduce(MonetaryAmount::add)
            .orElse(Money.of(0, "EUR"));
    }
}
