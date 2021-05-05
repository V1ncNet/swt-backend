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
public class Types implements Streamable<BeerType> {

    public static final Product.Id TYPE_PILS_ID = new Product.Id(UUID.fromString("b91384f9-1ba1-4f91-a78e-ce387ade341e"));
    public static final Product.Id TYPE_LAGER_ID = new Product.Id(UUID.fromString("65c8e363-cc41-40de-a1b8-f7529fc2b1f1"));
    public static final Product.Id TYPE_WHEAT_ID = new Product.Id(UUID.fromString("053fe46e-805a-4958-a3f8-971e8ee4abcd"));

    public static BeerType createPils() {
        return new BeerType(TYPE_PILS_ID, "Pils", Money.of(1.99, EURO));
    }

    public static BeerType createLager() {
        return new BeerType(TYPE_LAGER_ID, "Lager", Money.of(0.79, EURO));
    }

    public static BeerType createWheat() {
        return new BeerType(TYPE_WHEAT_ID, "Wheat", Money.of(0.99, EURO));
    }

    @Override
    public Iterator<BeerType> iterator() {
        return Arrays.asList(createPils(), createLager(), createWheat()).iterator();
    }
}
