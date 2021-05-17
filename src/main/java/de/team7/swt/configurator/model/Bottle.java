package de.team7.swt.configurator.model;

import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Metric;
import de.team7.swt.domain.quantity.Quantity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * An entity which encapsulates enumerable derivates of a beer bottle and its image location.
 *
 * @author Julian Albrecht
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
@Setter
public class Bottle extends Product {

    @Enumerated(EnumType.STRING)
    private Size size;

    @Enumerated(EnumType.STRING)
    private Color color;

    public Bottle(String name, MonetaryAmount price, Size size, Color color) {
        this(null, name, price, size, color);
    }

    protected Bottle(Id id, String name, MonetaryAmount price, Size size, Color color) {
        super(id, name, price);
        this.size = size;
        this.color = color;
    }

    /**
     * A collection of common beer bottle colors.
     *
     * @author Julian Albrecht
     */
    public enum Color {
        WHITE,
        BROWN,
        GREEN,
        ;
    }

    /**
     * A collection of common beer bottle sizes and their associated quantities.
     *
     * @author Julian Albrecht
     */
    @Getter
    @RequiredArgsConstructor
    public enum Size {
        SMALL(Quantity.of(0.33, Metric.LITER)),
        NORMAL(Quantity.of(0.5, Metric.LITER)),
        ;

        private final Quantity quantity;
    }
}
