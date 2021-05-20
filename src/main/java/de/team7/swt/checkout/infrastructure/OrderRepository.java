package de.team7.swt.checkout.infrastructure;

import de.team7.swt.checkout.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An JPA-{@link org.springframework.data.repository.Repository} for managing {@link Order} instances.
 *
 * @author Vincent Nadoll
 */
public interface OrderRepository extends JpaRepository<Order, Order.Id> {
}
