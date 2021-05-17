package de.team7.swt.inventory.infrastructure;

import de.team7.swt.domain.catalog.Product;
import de.team7.swt.inventory.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA {@link org.springframework.data.repository.Repository} for managing {@link InventoryItem} instances.
 *
 * @author Vincent Nadoll
 */
public interface Inventory extends JpaRepository<InventoryItem, InventoryItem.Id> {

    /**
     * Returns the inventory item the given {@link Product.Id} is related to.
     *
     * @param productId must not be {@literal null}
     * @return the found inventory item or {@literal Optional.empty()} if the item could not be found
     */
    Optional<InventoryItem> findByProductId(Product.Id productId);
}
