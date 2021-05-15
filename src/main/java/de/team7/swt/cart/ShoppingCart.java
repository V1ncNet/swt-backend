package de.team7.swt.cart;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import de.team7.swt.domain.shared.AggregateRoot;
import de.team7.swt.domain.shared.Identifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
@Getter
@Setter
public class ShoppingCart extends AggregateRoot<ShoppingCart.Id> implements Comparable<ShoppingCart> {

    @EmbeddedId
    @GeneratedValue(generator = "product-id")
    @GenericGenerator(name = "product-id", strategy = "dyob-id")
    @JsonUnwrapped
    private final ShoppingCart.Id id;

    @ElementCollection
    private List<ShoppingCartEntry> productList;

    public final boolean add(Product product, Quantity quantity){
        ShoppingCartEntry entry = new ShoppingCartEntry(product, quantity);
        return productList.add(entry);
    }

    public final boolean remove(ShoppingCartEntry entry){
        return  productList.remove(entry);
    }

    public ShoppingCart(Id id) {
        this.id = id;
    }

    public ShoppingCart() {
        this.id = null;
    }

    @Override
    public int compareTo(ShoppingCart o) {
        return 0;
    }
    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Id extends Identifier {
        public Id(@NonNull UUID id) {
            super(id);
        }
    }
}
