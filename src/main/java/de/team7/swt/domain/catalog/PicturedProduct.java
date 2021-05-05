package de.team7.swt.domain.catalog;

import de.team7.swt.domain.quantity.Metric;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.net.URI;
import javax.money.MonetaryAmount;
import javax.persistence.Entity;

/**
 * Extends the {@link Product} attributes by an optional image location field.
 *
 * @author Vincent Nadoll
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PicturedProduct extends Product {

    @Nullable
    private URI imageLocation;

    /**
     * Creates a new pictured product with given ID, name, price and image location.
     *
     * @param id            can be {@literal null}
     * @param name          must not be {@literal null} nor empty
     * @param price         must not be {@literal null}
     * @param imageLocation can be {@literal null}
     */
    protected PicturedProduct(Id id, String name, MonetaryAmount price, URI imageLocation) {
        super(id, name, price);
        this.imageLocation = imageLocation;
    }

    /**
     * Creates a new pictured product with given name, price and image location.
     *
     * @param name          must not be {@literal null} nor empty
     * @param price         must not be {@literal null}
     * @param imageLocation can be {@literal null}
     */
    public PicturedProduct(String name, MonetaryAmount price, URI imageLocation) {
        super(name, price);
        this.imageLocation = imageLocation;
    }

    /**
     * Creates a new pictured product with given name, price, metric and image location.
     *
     * @param name          must not be {@literal null} nor empty
     * @param price         must not be {@literal null}
     * @param metric        must not be {@literal null}
     * @param imageLocation can be {@literal null}
     */
    public PicturedProduct(String name, MonetaryAmount price, Metric metric, URI imageLocation) {
        super(name, price, metric);
        this.imageLocation = imageLocation;
    }

    /**
     * Creates a new pictured product with given ID, name, price, metric and image location.
     *
     * @param id            can be {@literal null}
     * @param name          must not be {@literal null} nor empty
     * @param price         must not be {@literal null}
     * @param metric        must not be {@literal null}
     * @param imageLocation can be {@literal null}
     */
    protected PicturedProduct(Id id, String name, MonetaryAmount price, Metric metric, URI imageLocation) {
        super(id, name, price, metric);
        this.imageLocation = imageLocation;
    }
}
