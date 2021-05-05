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
    private URI image;

    protected PicturedProduct(Id id, String name, MonetaryAmount price, URI image) {
        super(id, name, price);
        this.image = image;
    }

    public PicturedProduct(String name, MonetaryAmount price, URI image) {
        super(name, price);
        this.image = image;
    }

    public PicturedProduct(String name, MonetaryAmount price, Metric metric, URI image) {
        super(name, price, metric);
        this.image = image;
    }

    protected PicturedProduct(Id id, String name, MonetaryAmount price, Metric metric, URI image) {
        super(id, name, price, metric);
        this.image = image;
    }
}
