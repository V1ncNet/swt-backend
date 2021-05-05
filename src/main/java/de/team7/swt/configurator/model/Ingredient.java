package de.team7.swt.configurator.model;

import de.team7.swt.domain.catalog.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

/**
 * Entity class representing a beer ingredient.
 *
 * @author Julina Albrecht
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient extends Product {

    public Ingredient(String name, MonetaryAmount price) {
        this(null, name, price);
    }

    protected Ingredient(Id id, String name, MonetaryAmount price) {
        super(id, name, price);
        add("ingredient");
    }
}
