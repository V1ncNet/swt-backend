package de.team7.swt.configurator.model;

import de.team7.swt.domain.catalog.Product;
import lombok.SneakyThrows;
import org.springframework.data.util.Streamable;

import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import static de.team7.swt.configurator.model.Currencies.ZERO_EURO;

/**
 * @author Vincent Nadoll
 */
public class Labels implements Streamable<Label> {

    public static final Product.Id LABEL_BLACK_ID = new Product.Id(UUID.fromString("a36212dc-3323-4ec2-a227-509e6f1b4d2b"));
    public static final Product.Id LABEL_BLUE_ID = new Product.Id(UUID.fromString("c85f694b-8528-41d2-bdbc-816c2dbc7133"));
    public static final Product.Id LABEL_RED_ID = new Product.Id(UUID.fromString("eedf54a2-8747-47be-8d5a-c36a0cd3e1ab"));

    @SneakyThrows
    public static Label createBlack() {
        return new Label(LABEL_BLACK_ID, "Black", ZERO_EURO, URI.create("http://localhost:8080/red.png"));
    }

    @SneakyThrows
    public static Label createBlue() {
        return new Label(LABEL_BLUE_ID, "Blue", ZERO_EURO, URI.create("http://localhost:8080/red.png"));
    }

    @SneakyThrows
    public static Label createRed() {
        return new Label(LABEL_RED_ID, "Red", ZERO_EURO, URI.create("http://localhost:8080/red.png"));
    }

    @Override
    public Iterator<Label> iterator() {
        return Arrays.asList(createBlack(), createBlue(), createRed()).iterator();
    }
}
