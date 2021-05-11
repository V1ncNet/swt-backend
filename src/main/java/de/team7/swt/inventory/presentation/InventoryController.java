package de.team7.swt.inventory.presentation;

import de.team7.swt.domain.catalog.Product;
import de.team7.swt.domain.quantity.Quantity;
import de.team7.swt.inventory.infrastructure.Inventory;
import de.team7.swt.inventory.model.InventoryItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Stateful MVC-Controller to manage the product stock.
 *
 * @author Vincent Nadoll
 */
@Controller
@RequiredArgsConstructor
class InventoryController {

    private final Inventory inventory;

    /**
     * Exposes the {@literal stock.html}-page.
     *
     * @param model Spring's UI model
     * @return 200
     */
    @GetMapping("/stock")
    String stock(Model model) {
        List<InventoryItem> stock = inventory.findAll();
        model.addAttribute("inventory", stock);
        model.addAttribute("formData", new ItemFormData());
        return "stock";
    }

    /**
     * Exposes an endpoint to update an item's quantity.
     *
     * @param formData submitted form data
     * @return a redirection to {@link #stock(Model)}
     */
    @PostMapping("/stock")
    String editItem(@ModelAttribute ItemFormData formData) {
        InventoryItem item = inventory.findById(formData.itemId)
            .orElseThrow(IllegalStateException::new);
        Product product = item.getProduct();

        Quantity quantity = product.from(formData.itemQuantityAmountOffset);
        item.increase(quantity);
        inventory.save(item);

        return "redirect:/stock#" + formData.itemId;
    }

    /**
     * DTO containing the mandatory attributes to change the item's amount.
     *
     * @author Vincent Nadoll
     */
    @Getter
    @Setter
    @EqualsAndHashCode
    static class ItemFormData {
        private InventoryItem.Id itemId;
        private double itemQuantityAmountOffset = 0;
    }
}