package de.team7.swt.configurator.model;

import de.team7.swt.domain.catalog.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;
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

    private URI image;

    @Enumerated(EnumType.STRING)
    private BottleSize size;

    @Enumerated(EnumType.STRING)
    private BottleColor color;

    public Bottle(String name, MonetaryAmount price) {
        super(name, price);
    }
}
