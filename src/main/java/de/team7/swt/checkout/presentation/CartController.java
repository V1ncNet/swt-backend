package de.team7.swt.checkout.presentation;

import de.team7.swt.checkout.application.OrderCompletionReport;
import de.team7.swt.checkout.infrastructure.OrderRepository;
import de.team7.swt.checkout.model.Cart;
import de.team7.swt.checkout.model.Order;
import de.team7.swt.domain.catalog.Catalog;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.web.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.data.util.Streamable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * Stateful controller which handles requests and responses regarding checkout. Each new HTTP session gets its own
 * {@link Cart} instance which times out after 30 minutes (by default).
 *
 * @author Vincent Nadoll
 * @see org.springframework.boot.web.servlet.server.Session
 */
@Controller
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
class CartController {

    public static final String UNIQUE_DESCRIPTOR = "Komponente";

    private final Cart cart;
    private final OrderRepository repository;
    private final Catalog<Product> productCatalog;

    /**
     * Retrieves the session bound cart instance.
     *
     * @return 200 - cart
     */
    @GetMapping
    ResponseEntity<Cart> retrieve() {
        return ResponseEntity.ok(unwrapProxy(cart));
    }

    /**
     * Creates or updates one cart item for given {@link Product} ID.
     *
     * @param product must not be {@literal null}
     * @return 200 - updated cart
     */
    @PutMapping
    ResponseEntity<Cart> addItem(@RequestParam("product_id") Product product) {
        if (cartContainsCategoriesFrom(product)) {
            throw new ValidationException(String.format(
                "Cart already contains product w/ any categories like %s but must be unique",
                streamCategories(product).collect(Collectors.toSet())
            ));
        }

        if (cart.contains(product)) {
            throw new ValidationException("Cart already contains this product");
        }

        cart.set(product, product.from(1));

        return ResponseEntity.ok(unwrapProxy(cart));
    }

    private boolean cartContainsCategoriesFrom(Product product) {
        String[] categories = streamCategories(product)
            .filter(not(categoryEquals(UNIQUE_DESCRIPTOR)))
            .toArray(String[]::new);
        return product.contains(UNIQUE_DESCRIPTOR) && cart.containsAny(categories);
    }

    private Stream<String> streamCategories(Product product) {
        return product.getCategories().stream();
    }

    private Predicate<String> categoryEquals(String other) {
        return category -> Objects.equals(category, other);
    }

    /*
     * Cart is managed by the IoC-Container. Thus, the cart instance is actually a proxy reflection containing some ugly
     * properties that are not desirable in a JSON document.
     * Source: https://stackoverflow.com/a/8121671
     */
    @SneakyThrows
    private static Cart unwrapProxy(Cart cart) {
        if (AopUtils.isAopProxy(cart) && cart instanceof Advised) {
            Object target = ((Advised) cart).getTargetSource().getTarget();
            return (Cart) target;
        } else {
            return cart;
        }
    }

    /**
     * Creates and completes a new Order and generates a completion report afterwards.
     *
     * @param amount multiplier for each item in the cart
     * @return 200 - order completion report
     */
    @PostMapping("/checkout")
    ResponseEntity<OrderCompletionReport> checkout(@RequestParam int amount,
                                                   @RequestParam("crate_size") CrateSize crateSize) {
        verify(cart);

        Order order = new Order();
        transferItems(cart, order, Long.valueOf(crateSize.times(amount)).intValue());
        order.complete();
        repository.save(order);
        cart.clear();

        return ResponseEntity.ok(OrderCompletionReport.success(order));
    }

    private void verify(Cart cart) {
        Set<String> categories = getMandatoryCategories();
        if (!cart.containsAll(categories.toArray(String[]::new))) {
            throw new ValidationException(String.format("Cart must contain products of all categories %s", categories));
        }
    }

    private Set<String> getMandatoryCategories() {
        return productCatalog.findByCategory(UNIQUE_DESCRIPTOR)
            .map(Product::getCategories)
            .flatMap(Streamable::stream)
            .toSet();
    }

    private void transferItems(Cart cart, Order order, int amount) {
        try {
            cart.addItemsTo(order, amount);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Parameter \"amount\" must not be negative or zero");
        }
    }
}
