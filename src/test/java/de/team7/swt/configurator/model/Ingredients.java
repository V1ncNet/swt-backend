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
public class Ingredients implements Streamable<Ingredient> {

    public static final Product.Id INGREDIENT_CLOVE_ID = new Product.Id(UUID.fromString("7a2705a3-7cdf-45ee-891d-295d16187c05"));
    public static final Product.Id INGREDIENT_CINNAMON_ID = new Product.Id(UUID.fromString("1b9cbeb8-7811-4101-ac63-f2a3bfb8a6a7"));
    public static final Product.Id INGREDIENT_PEPPER_ID = new Product.Id(UUID.fromString("b1dd6d6c-0b06-4dfc-97f8-23474aa3277b"));

    public static Ingredient createClove() {
        return new Ingredient(INGREDIENT_CLOVE_ID, "Clove", Money.of(1.99, EURO));
    }

    public static Ingredient createCinnamon() {
        return new Ingredient(INGREDIENT_CINNAMON_ID, "Cinnamon", Money.of(0.99, EURO));
    }

    public static Ingredient createPepper() {
        return new Ingredient(INGREDIENT_PEPPER_ID, "Pepper", Money.of(0.79, EURO));
    }

    @Override
    public Iterator<Ingredient> iterator() {
        return Arrays.asList(createClove(), createCinnamon(), createPepper()).iterator();
    }
}
