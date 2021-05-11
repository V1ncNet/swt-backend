package de.team7.swt.inventory.infrastructure;

import de.team7.swt.inventory.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA {@link org.springframework.data.repository.Repository} for managing {@link InventoryItem} instances.
 *
 * @author Vincent Nadoll
 */
public interface Inventory extends JpaRepository<InventoryItem, InventoryItem.Id> {
}
