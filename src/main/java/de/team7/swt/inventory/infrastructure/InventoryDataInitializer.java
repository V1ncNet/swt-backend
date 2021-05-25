package de.team7.swt.inventory.infrastructure;

import de.team7.swt.domain.catalog.Catalog;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.infrastructure.DataInitializer;
import de.team7.swt.inventory.model.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * {@link DataInitializer} implementation which creates 100 inventory items of each product in the {@link Catalog}.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
class InventoryDataInitializer implements DataInitializer {

    private final Catalog<Product> catalog;
    private final Inventory inventory;

    @Override
    public void initialize() {
        catalog.findAll().stream()
            .map(createInventoryItem(500))
            .forEach(inventory::save);
    }

    private Function<Product, InventoryItem> createInventoryItem(int amount) {
        return product -> new InventoryItem(product, product.from(amount));
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER + 1;
    }
}
