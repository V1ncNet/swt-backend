package de.team7.swt.domain.catalog;

import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import static de.team7.swt.domain.catalog.Currencies.EURO;

/**
 * @author Vincent Nadoll
 */
public class Flavours implements Streamable<Product> {

    public static final Product.Id FLAVOUR_CHOCOLATE_ID = new Product.Id(UUID.fromString("cc552e26-6453-4e82-82a4-85c16cc3c4c5"));
    public static final Product.Id FLAVOUR_COCONUT_ID = new Product.Id(UUID.fromString("532e0559-e67f-44ca-85d4-2f7f48c89063"));
    public static final Product.Id FLAVOUR_COOKIE_ID = new Product.Id(UUID.fromString("b2251bc1-4a6b-46e8-9cf8-b1d32e982f71"));

    public static Product createChocolate() {
        Product product = new Product(FLAVOUR_CHOCOLATE_ID, "chocolate", Money.of(1.99, EURO));
        product.add("flavour");
        return product;
    }

    public static Product createCoconut() {
        Product product = new Product(FLAVOUR_COCONUT_ID, "coconut", Money.of(0.79, EURO));
        product.add("flavour");
        return product;
    }

    public static Product createCookie() {
        Product product = new Product(FLAVOUR_COOKIE_ID, "cookie", Money.of(0.99, EURO));
        product.add("flavour");
        return product;
    }

    @Override
    public Iterator<Product> iterator() {
        return Arrays.asList(createChocolate(), createCoconut(), createCookie()).iterator();
    }
}
