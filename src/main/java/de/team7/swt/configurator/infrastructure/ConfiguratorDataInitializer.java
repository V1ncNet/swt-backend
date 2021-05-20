package de.team7.swt.configurator.infrastructure;

import de.team7.swt.configurator.model.Bottle;
import de.team7.swt.configurator.model.Bottle.Color;
import de.team7.swt.configurator.model.Bottle.Size;
import de.team7.swt.domain.catalog.Catalog;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.infrastructure.DataInitializer;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Consumer;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

/**
 * {@link DataInitializer} implementation setting up the application-wide product catalog.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
class ConfiguratorDataInitializer implements DataInitializer {

    private static final CurrencyUnit EURO = Monetary.getCurrency("EUR");
    private static final MonetaryAmount ZERO_EURO = Money.zero(EURO);

    private static final String[] kinds = new String[]{
        "Altbier",
        "Berliner Weisse",
        "Bock",
        "Dunkel",
        "Export",
        "Gose",
        "Kölsch",
        "Lager",
        "Märzen",
        "Oktoberfest",
        "Pils",
        "Rauch",
        "Schwarz",
        "Weizen",
        "Zwickel & Keller",
    };

    private static final String[] flavours = new String[]{
        "Leicht/Erfrischend",
        "Spritzig/Erfrischend",
        "Bluming/Fruchtig",
        "Hopfig/Bitter",
        "Karamell/Honig",
        "Würzig/Kräuteraroma",
        "Stark/Herzhaft",
        "Dunkel/Vollmundig",
        "im Fass gereift",
        "Experimentell",
    };

    private static final String[] ingredientsBeverage = new String[]{
        "Cola",
        "Sprite",
        "Fanta",
        "Vodka",
        "Tequila",
        "Rum",
    };

    private static final String[] ingredientsFruityJuicy = new String[]{
        "Himbeeren",
        "Limetten",
        "Orangen",
        "Erdbeeren",
        "Mango",
        "Kirsche",
        "Karotte",
    };

    private static final String[] ingredientsCrazyStuff = new String[]{
        "Schokolade",
        "Brezel",
        "Knoblauch",
        "Rosmarin",
        "Chilli",
        "Honig",
        "Karamell",
        "Vanille",
    };

    private final Catalog<Product> catalog;

    @Override
    public void initialize() {
        Arrays.stream(kinds)
            .map(name -> new Product(name, ZERO_EURO))
            .peek(addCategory("Biersorte"))
            .peek(addCategory("Komponente"))
            .forEach(catalog::save);

        catalog.save(createBottle("Weißes Glas (0,5 l)", Size.NORMAL, Color.WHITE));
        catalog.save(createBottle("Braunes Glas (0,5 l)", Size.NORMAL, Color.BROWN));
        catalog.save(createBottle("Grünes Glas (0,5 l)", Size.NORMAL, Color.GREEN));

        catalog.save(createBottle("Weißes Glas (0,33 l)", Size.SMALL, Color.WHITE));
        catalog.save(createBottle("Braunes Glas (0,33 l)", Size.SMALL, Color.BROWN));
        catalog.save(createBottle("Grünes Glas (0,33 l)", Size.SMALL, Color.GREEN));

        Arrays.stream(flavours)
            .map(name -> new Product(name, ZERO_EURO))
            .peek(addCategory("Geschmacksrichtung"))
            .peek(addCategory("Komponente"))
            .forEach(catalog::save);

        Arrays.stream(ingredientsBeverage)
            .map(name -> new Product(name, Money.of(0.5, EURO)))
            .peek(addCategory("Zutat"))
            .peek(addCategory("Getränk"))
            .forEach(catalog::save);
        Arrays.stream(ingredientsFruityJuicy)
            .map(name -> new Product(name, Money.of(1.5, EURO)))
            .peek(addCategory("Zutat"))
            .peek(addCategory("Frucht & Saft"))
            .forEach(catalog::save);
        Arrays.stream(ingredientsCrazyStuff)
            .map(name -> new Product(name, Money.of(1, EURO)))
            .peek(addCategory("Zutat"))
            .peek(addCategory("Crazy Stuff"))
            .forEach(catalog::save);
    }

    private static <T extends Product> Consumer<T> addCategory(String category) {
        return product -> product.add(category);
    }

    private static Bottle createBottle(String name, Size size, Color color) {
        Bottle bottle = new Bottle(name, ZERO_EURO, size, color);
        bottle.add("Flasche");
        bottle.add("Komponente");
        return bottle;
    }
}
