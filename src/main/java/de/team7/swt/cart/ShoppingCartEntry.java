package de.team7.swt.cart;

import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
public class ShoppingCartEntry implements Serializable {

    @Getter
    @Setter
    private Product product;

    @Getter
    @Setter
    private Quantity quantity;

}
