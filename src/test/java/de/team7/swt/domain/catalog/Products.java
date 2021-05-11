package de.team7.swt.domain.catalog;

import de.team7.swt.configurator.model.Bottles;
import de.team7.swt.configurator.model.Labels;
import org.springframework.data.util.Streamable;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Vincent Nadoll
 */
public class Products implements Streamable<Product> {

    private final Types beerTypes = new Types();
    private final Bottles bottles = new Bottles();
    private final Flavours flavours = new Flavours();
    private final Ingredients ingredients = new Ingredients();
    private final Labels labels = new Labels();

    @Override
    public Iterator<Product> iterator() {
        return Stream.of(beerTypes.get(), bottles.get(), flavours.get(), ingredients.get(), labels.get())
            .flatMap(Function.identity()).map(Product.class::cast).iterator();
    }
}
