package de.team7.swt.checkout.presentation;

import de.team7.swt.checkout.model.Cart;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Vincent Nadoll
 */
@Configuration
class CartConfiguration {

    @Bean
    public Cart cart() {
        return new Cart();
    }
}
