package de.team7.swt.checkout.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Abstraction of a shopping cart.
 *
 * @author Julian Albrecht
 * @author Vincent Nadoll
 */
public class Cart implements Totalable<CartItem> {

    private final Map<Product, CartItem> items = new LinkedHashMap<>();

    @JsonGetter
    private Collection<CartItem> getItems() {
        return toList();
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

    /**
     * Retrieves a {@link CartItem} for its ID.
     *
     * @param id must not be {@literal null}
     * @return {@link Optional} wrapped {@link CartItem}; {@literal Optional.empty()} if not found
     */
    public Optional<CartItem> retrieve(CartItem.Id id) {
        Assert.notNull(id, "Cart item ID must not be null");
        return items.values().stream()
            .filter(idEquals(id))
            .findFirst();
    }

    private static Predicate<CartItem> idEquals(CartItem.Id id) {
        return item -> Objects.equals(item.getId(), id);
    }

    /**
     * Deletes a {@link CartItem} by its ID.
     *
     * @param id must not be {@literal null}
     */
    public void delete(CartItem.Id id) {
        Assert.notNull(id, "Cart item ID must not be null");
        retrieve(id)
            .map(CartItem::getProduct)
            .ifPresent(items::remove);
    }

    /**
     * Places all {@link CartItem}s into the given {@link Order}.
     *
     * @param order must not be {@literal null}
     */
    public void addItemsTo(Order order) {
        Assert.notNull(order, "Order must not be null");

        forEach(addTo(order));
    }

    private static Consumer<CartItem> addTo(Order order) {
        return item -> order.addItem(item.getProduct(), item.getQuantity());
    }

    /**
     * Clears this cart.
     */
    public void clear() {
        items.clear();
    }

    @Override
    public Iterator<CartItem> iterator() {
        return items.values().iterator();
    }
}
