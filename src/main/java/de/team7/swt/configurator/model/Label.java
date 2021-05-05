package de.team7.swt.configurator.model;

import de.team7.swt.domain.catalog.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.net.URL;
import javax.money.MonetaryAmount;
import javax.persistence.Entity;

/**
 * An entity which represents an identifiable label location.
 *
 * @author Julian Albrecht
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Label extends Product {

    @NonNull
    private URL image;

    protected Label(Id id, String name, MonetaryAmount price, @NonNull URL image) {
        super(id, name, price);
        this.image = image;
    }

    public Label(String name, MonetaryAmount price) {
        super(name, price);
    }
}
