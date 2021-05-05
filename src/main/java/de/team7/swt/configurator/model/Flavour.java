package de.team7.swt.configurator.model;

import de.team7.swt.domain.catalog.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

/**
 * An entity class representing a beer flavour.
 *
 * @author Julian Albrecht
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Flavour extends Product {

    public Flavour(String name, MonetaryAmount price) {
        this(null, name, price);
    }

    protected Flavour(Id id, String name, MonetaryAmount price) {
        super(id, name, price);
        add("flavour");
    }
}
