package de.team7.swt.checkout.presentation;

import de.team7.swt.checkout.model.Cart;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

/**
 * Configure {@link Cart} to be controlled by the IoC container. Once the servlet container creates a new session a new
 * cart instance is created and bound.
 *
 * @author Vincent Nadoll
 */
@Configuration
class CartConfiguration {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Cart cart() {
        return new Cart();
    }
}
