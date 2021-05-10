package de.team7.swt.inventory.presentation;

import de.team7.swt.inventory.infrastructure.Inventory;
import de.team7.swt.inventory.model.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        return "stock";
    }
}
