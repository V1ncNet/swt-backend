package de.team7.swt.checkout.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import de.team7.swt.domain.shared.Identifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.util.Assert;

import java.util.UUID;
import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

/**
 * An {@link de.team7.swt.domain.shared.Entity} representing {@link Product} and {@link Quantity} that is intended to be
 * purchased as part of an {@link Order}.
 *
 * @author Vincent Nadoll
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class LineItem extends de.team7.swt.domain.shared.Entity<LineItem.Id> implements Priced {

    @EmbeddedId
    @GeneratedValue(generator = "lineItem-id")
    @GenericGenerator(name = "lineItem-id", strategy = "dyob-id")
    @JsonUnwrapped
    private final LineItem.Id id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "product_id")) // Avoids ambiguous column id
    @JsonUnwrapped(prefix = "product_")
    private Product.Id productId;

    @JsonProperty("product_name")
    private String productName;

    @NumberFormat(pattern = "0.00 ¤")
    @Convert(disableConversion = true)
    @Type(type = "de.team7.swt.domain.infrastructure.money.MonetaryAmountType")
    private MonetaryAmount price;

    private Quantity quantity;

    /**
     * Creates a new {@link LineItem} with given product and quantity.
     *
     * @param product  must not be {@literal null}
     * @param quantity must not be {@literal null}
     */
    LineItem(Product product, Quantity quantity) {
        this(null, product, quantity);
    }

    /**
     * Creates a new {@link LineItem} with given ID, product and quantity.
     *
     * @param id       can be {@literal null}
     * @param product  must not be {@literal null}
     * @param quantity must not be {@literal null}
     */
    protected LineItem(LineItem.Id id, Product product, Quantity quantity) {
        Assert.notNull(product, "Product must not be null");
        Assert.notNull(quantity, "Quantity must not be null");
        product.verify(quantity);

        this.id = id;
        this.productId = product.getId();
        this.productName = product.getName();
        this.price = product.getPrice().multiply(quantity.getAmount());
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("productId", productId)
            .append("productName", productName)
            .append("price", price)
            .append("quantity", quantity)
            .toString();
    }

    /**
     * Value object representing an {@link LineItem}'s primary identifier.
     *
     * @author Vincent Nadoll
     */
    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Id extends Identifier {
        public Id(@NonNull UUID id) {
            super(id);
        }
    }
}
