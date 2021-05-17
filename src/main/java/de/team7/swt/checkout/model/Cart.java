package de.team7.swt.checkout.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import org.javamoney.moneta.Money;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import javax.money.MonetaryAmount;

/**
 * Abstraction of a shopping cart.
 *
 * @author Vincent Nadoll
 */
public class Cart {

    private final Map<Product, CartItem> items = new LinkedHashMap<>();

    @JsonGetter
    private Collection<CartItem> getItems() {
        return items.values();
    }

    /**
     * Saves given {@link Product} and {@link Quantity} and creates or updates an existing {@link CartItem}.
     *
     * @param product  must not be {@literal null}
     * @param quantity must not be {@literal null}
     * @return saved {@link CartItem}
     */
    public CartItem save(Product product, Quantity quantity) {
        Assert.notNull(product, "Product must not be null");
        Assert.notNull(quantity, "Quantity must not be null");

        return items.compute(product, saveWith(quantity));
    }

    private static BiFunction<Product, CartItem, CartItem> saveWith(Quantity quantity) {
        return (product, cartItem) -> Objects.isNull(cartItem)
            ? new CartItem(product, quantity)
            : cartItem.add(quantity);
    }

    public void addItemsTo(Order order) {
        Assert.notNull(order, "Order must not be null");

        items.values().forEach(addTo(order));
    }

    private static Consumer<CartItem> addTo(Order order) {
        return item -> order.addItem(item.getProduct(), item.getQuantity());
    }

    public void clear() {
        items.clear();
    }

    public MonetaryAmount getTotal() {
        return items.values()
            .stream()
            .map(CartItem::getPrice)
            .reduce(MonetaryAmount::add)
            .orElse(Money.of(0, "EUR"));
    }
}
