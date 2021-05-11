package de.team7.swt.domain.catalog;

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

/**
 * @author Vincent Nadoll
 */
public interface Currencies {

    CurrencyUnit EURO = Monetary.getCurrency("EUR");

    MonetaryAmount ZERO_EURO = Money.zero(EURO);
}
