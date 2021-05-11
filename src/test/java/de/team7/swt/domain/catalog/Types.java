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
public class Types implements Streamable<Product> {

    public static final Product.Id TYPE_PILS_ID = new Product.Id(UUID.fromString("b91384f9-1ba1-4f91-a78e-ce387ade341e"));
    public static final Product.Id TYPE_LAGER_ID = new Product.Id(UUID.fromString("65c8e363-cc41-40de-a1b8-f7529fc2b1f1"));
    public static final Product.Id TYPE_WHEAT_ID = new Product.Id(UUID.fromString("053fe46e-805a-4958-a3f8-971e8ee4abcd"));

    public static Product createPils() {
        Product product = new Product(TYPE_PILS_ID, "Pils", Money.of(1.99, EURO));
        product.add("beertype");
        return product;
    }

    public static Product createLager() {
        Product product = new Product(TYPE_LAGER_ID, "Lager", Money.of(0.79, EURO));
        product.add("beertype");
        return product;
    }

    public static Product createWheat() {
        Product product = new Product(TYPE_WHEAT_ID, "Wheat", Money.of(0.99, EURO));
        product.add("beertype");
        return product;
    }

    @Override
    public Iterator<Product> iterator() {
        return Arrays.asList(createPils(), createLager(), createWheat()).iterator();
    }
}
