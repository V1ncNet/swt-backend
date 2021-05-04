package de.team7.swt.configurator.model;

import de.team7.swt.domain.catalog.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient extends Product {

    public Ingredient(String name, MonetaryAmount price) {
        super(name, price);
    }
}
