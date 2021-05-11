package de.team7.swt.configurator.model;

import de.team7.swt.configurator.model.Bottle.Color;
import de.team7.swt.configurator.model.Bottle.Size;
import de.team7.swt.domain.catalog.Product;
import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;

import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import static de.team7.swt.domain.catalog.Currencies.EURO;

/**
 * @author Vincent Nadoll
 */
public class Bottles implements Streamable<Bottle> {

    public static final Product.Id BOTTLE_05_BROWN_ID = new Product.Id(UUID.fromString("2936852b-c88e-4e50-8b77-11999f0f1b34"));
    public static final Product.Id BOTTLE_033_GREEN_ID = new Product.Id(UUID.fromString("3549c012-12c2-4211-bb3b-43a580e0ba8c"));

    public static Bottle create05Brown() {
        return new Bottle(BOTTLE_05_BROWN_ID, "0.5l - Brown Glass", Money.of(0.70, EURO), URI.create("http://localhost:8080/05-brown.png"), Size.NORMAL, Color.BROWN);
    }

    public static Bottle create033Green() {
        return new Bottle(BOTTLE_033_GREEN_ID, "0.33l - Green Glass", Money.of(0.59, EURO), URI.create("http://localhost:8080/033-green.png"), Size.SMALL, Color.GREEN);
    }

    @Override
    public Iterator<Bottle> iterator() {
        return Arrays.asList(create05Brown(), create033Green()).iterator();
    }
}
