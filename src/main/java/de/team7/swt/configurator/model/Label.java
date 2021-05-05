package de.team7.swt.configurator.model;

import de.team7.swt.domain.catalog.PicturedProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;
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
public class Label extends PicturedProduct {

    public Label(String name, MonetaryAmount price, URI imageLocation) {
        this(null, name, price, imageLocation);
    }

    protected Label(Id id, String name, MonetaryAmount price, URI imageLocation) {
        super(id, name, price, imageLocation);
        add("label");
    }
}
