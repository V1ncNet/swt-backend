package de.team7.swt.inventory.application;

import de.team7.swt.checkout.application.OrderCompleted;
import de.team7.swt.checkout.model.OrderCompletionFailure;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Component listening on events emitted by the checkout component.
 *
 * @author Vincent Nadoll
 */
@Component
@RequiredArgsConstructor
class OrderEventListener {

    private final OrderHandler handler;

    /**
     * {@link EventListener} for listening on the {@link OrderCompleted} event.
     *
     * @param event must not be {@literal null}
     * @throws OrderCompletionFailure in case the order could not be verified successfully
     */
    @EventListener
    public void on(OrderCompleted event) throws OrderCompletionFailure {
        handler.verify(event.getOrder());
    }
}
