package de.team7.swt.inventory.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * {@link Exception} is thrown in case the given {@link InventoryItem} is not having enough quantity to subtract from.
 *
 * @author Vincent Nadoll
 */
@Getter
@NoArgsConstructor(force = true)
public class InsufficientQuantityException extends Exception {

    @Nullable
    private final InventoryItem item;

    /**
     * Creates a new exception with given message and {@link InventoryItem}.
     *
     * @param message must not be {@literal null}
     * @param item    can be {@literal null}
     */
    public InsufficientQuantityException(String message, InventoryItem item) {
        super(message);
        this.item = item;
    }
}
