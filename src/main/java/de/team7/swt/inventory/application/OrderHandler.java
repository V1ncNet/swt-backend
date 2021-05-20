package de.team7.swt.inventory.application;

import de.team7.swt.checkout.application.LineItemCompletion;
import de.team7.swt.checkout.application.OrderCompletionReport;
import de.team7.swt.checkout.model.LineItem;
import de.team7.swt.checkout.model.Order;
import de.team7.swt.checkout.model.OrderCompletionFailure;
import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import de.team7.swt.inventory.infrastructure.Inventory;
import de.team7.swt.inventory.model.InsufficientQuantityException;
import de.team7.swt.inventory.model.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Interface between inventory and checkout component handling quantity checks and updates on {@link InventoryItem}s.
 *
 * @author Vincent Nadoll
 * @see OrderHandler
 */
@Service
@RequiredArgsConstructor
class OrderHandler {

    private final Inventory inventory;

    /**
     * Verifies the order and decreases its item quantity by the amount of ordered items.
     *
     * @param order must not be {@literal null}
     * @throws OrderCompletionFailure in case the order could not be completed successfully
     */
    void verify(Order order) throws OrderCompletionFailure {
        Assert.notNull(order, "Order must not be null");
        List<LineItemCompletion> completions = order.map(this::verify).toList();
        OrderCompletionReport.of(order, Streamable.of(completions)).verify();
    }

    private LineItemCompletion verify(LineItem lineItem) {
        Product.Id productId = lineItem.getProductId();
        return inventory.findByProductId(productId)
            .map(inventoryItem -> verify(inventoryItem, lineItem))
            .orElseGet(() -> LineItemCompletion.skip(lineItem));
    }

    private LineItemCompletion verify(InventoryItem inventoryItem, LineItem lineItem) {
        Quantity quantity = lineItem.getQuantity();

        if (quantity.isNegative()) {
            return LineItemCompletion.error(lineItem, "Requested amount must not be negative");
        } else if (quantity.isZero()) {
            return LineItemCompletion.skip(lineItem);
        } else if (!inventoryItem.hasSufficientStock(quantity)) {
            return LineItemCompletion.error(lineItem, "Number of items requested by the line item is greater than "
                + "the number of available items");
        }

        LineItemCompletion success = LineItemCompletion.success(lineItem);
        decreaseQuantityAndSave(inventoryItem, quantity);
        return success;
    }

    private void decreaseQuantityAndSave(InventoryItem item, Quantity quantity) {
        try {
            item.decrease(quantity);
            inventory.save(item);
        } catch (InsufficientQuantityException e) {
            // shouldn't occur
            throw new IllegalStateException(e);
        }
    }
}
