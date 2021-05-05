package de.team7.swt.configurator.model;

import de.team7.swt.domain.catalog.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

/**
 * Entity class representing a generic type of beer.
 *
 * @author Julian Albrecht
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class BeerType extends Product {

    public BeerType(String name, MonetaryAmount price) {
        this(null, name, price);
    }

    protected BeerType(Id id, String name, MonetaryAmount price) {
        super(id, name, price);
        add("beertype");
    }
}
