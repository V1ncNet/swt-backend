package de.team7.swt.checkout.presentation;

import de.team7.swt.checkout.model.Cart;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.web.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Stateful controller which handles requests and responses regarding checkout. Each new HTTP session gets its own
 * {@link Cart} instance which times out after 30 minutes (by default).
 *
 * @author Vincent Nadoll
 * @see org.springframework.boot.web.servlet.server.Session
 */
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class CheckoutController {

    private final Cart cart;

    /**
     * Retrieves the session bound cart instance.
     *
     * @return 200 - cart
     */
    @GetMapping("/cart")
    ResponseEntity<Cart> retrieve() {
        return ResponseEntity.ok(unwrapProxy(cart));
    }

    /**
     * Creates or updates a cart item for given {@link Product} ID and amount.
     *
     * @param product must not be {@literal null}
     * @param amount  must not be {@literal null}
     * @return 200 - updated cart
     */
    @PostMapping("/cart")
    ResponseEntity<Cart> addItem(@RequestParam("productId") Product product, @RequestParam Number amount) {
        if (amount.doubleValue() <= 0) {
            throw new ValidationException("Parameter \"amount\" must not be negative or zero");
        }

        cart.save(product, product.from(amount.doubleValue()));

        return ResponseEntity.ok(unwrapProxy(cart));
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
}
