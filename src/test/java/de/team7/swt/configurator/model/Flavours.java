package de.team7.swt.configurator.model;

import de.team7.swt.domain.catalog.Product;
import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import static de.team7.swt.configurator.model.Currencies.EURO;

/**
 * @author Vincent Nadoll
 */
public class Flavours implements Streamable<Flavour> {

    public static final Product.Id FLAVOUR_CHOCOLATE_ID = new Product.Id(UUID.fromString("cc552e26-6453-4e82-82a4-85c16cc3c4c5"));
    public static final Product.Id FLAVOUR_COCONUT_ID = new Product.Id(UUID.fromString("532e0559-e67f-44ca-85d4-2f7f48c89063"));
    public static final Product.Id FLAVOUR_COOKIE_ID = new Product.Id(UUID.fromString("b2251bc1-4a6b-46e8-9cf8-b1d32e982f71"));

    public static Flavour createChocolate() {
        return new Flavour(FLAVOUR_CHOCOLATE_ID, "chocolate", Money.of(1.99, EURO));
    }

    public static Flavour createCoconut() {
        return new Flavour(FLAVOUR_COCONUT_ID, "coconut", Money.of(0.79, EURO));
    }

    public static Flavour createCookie() {
        return new Flavour(FLAVOUR_COOKIE_ID, "cookie", Money.of(0.99, EURO));
    }

    @Override
    public Iterator<Flavour> iterator() {
        return Arrays.asList(createChocolate(), createCoconut(), createCookie()).iterator();
    }
}
