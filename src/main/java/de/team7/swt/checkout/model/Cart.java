package de.team7.swt.checkout.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

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
    public CartItem add(Product product, Quantity quantity) {
        Assert.notNull(product, "Product must not be null");
        Assert.notNull(quantity, "Quantity must not be null");

        return items.compute(product, saveWith(quantity, add(quantity)));
    }

    private static UnaryOperator<CartItem> add(Quantity quantity) {
        return cartItem -> cartItem.add(quantity);
    }

    /**
     * Saves given {@link Product} and {@link Quantity} and creates or overrides a {@link CartItem}.
     *
     * @param product  must not be {@literal null}
     * @param quantity must not be {@literal null}
     * @return overridden {@link CartItem}
     */
    public CartItem set(Product product, Quantity quantity) {
        Assert.notNull(product, "Product must not be null");
        Assert.notNull(quantity, "Quantity must not be null");

        return items.compute(product, saveWith(quantity, override(quantity)));
    }

    private BiConsumer<Product, CartItem> set(int amount) {
        return (product, cartItem) -> set(product, product.from(amount));
    }

    private static BiFunction<Product, CartItem, CartItem> saveWith(Quantity quantity,
                                                                    UnaryOperator<CartItem> factory) {
        return (product, cartItem) -> Objects.isNull(cartItem)
            ? new CartItem(product, quantity)
            : factory.apply(cartItem);
    }

    private static UnaryOperator<CartItem> override(Quantity quantity) {
        return item -> item.create(quantity);
    }

    /**
     * Retrieves a {@link CartItem} for its ID.
     *
     * @param id must not be {@literal null}
     * @return {@link Optional} wrapped {@link CartItem}; {@literal Optional.empty()} if not found
     */
    public Optional<CartItem> retrieve(CartItem.Id id) {
        Assert.notNull(id, "Cart item ID must not be null");
        return stream()
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
     * Places all {@link CartItem}s with given amount into the given {@link Order}.
     *
     * @param order  must not be {@literal null}
     * @param amount must not be zero or negative
     */
    public void addItemsTo(Order order, int amount) {
        Assert.isTrue(amount > 0, "Amount must not be zero or negative");

        items.forEach(set(amount));
        addItemsTo(order);
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
     * Indicates whether the given {@link Product} is contained by this cart.
     *
     * @param product must not be {@literal null}
     * @return {@literal true} if this cart contains the argument; {@literal false} otherwise
     */
    public boolean contains(Product product) {
        Assert.notNull(product, "Product must not be null");
        return items.containsKey(product);
    }

    /**
     * Indicates whether this cart's products are assigned to any of the given categories.
     *
     * @param categories must not be {@literal null}
     * @return {@literal true} if this cart contains products assigned to any given categories; {@literal false}
     *     otherwise
     */
    public boolean containsAny(String... categories) {
        return items.keySet().stream()
            .anyMatch(subsetOf(categories));
    }

    private Predicate<Product> subsetOf(String[] categories) {
        return product -> !Collections.disjoint(product.getCategories().toSet(), Arrays.asList(categories));
    }

    /**
     * Indicates whether this cart's products are assigned to all of the given categories.
     *
     * @param categories must not be {@literal null};
     * @return {@literal true} if this cart all products assigned to all given categories; {@literal false} otherwise
     */
    public boolean containsAll(String... categories) {
        List<String> categoryList = Arrays.asList(categories);
        return items.keySet()
            .stream()
            .map(Product::getCategories)
            .flatMap(Streamable::stream)
            .collect(Collectors.toSet())
            .containsAll(categoryList);
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
