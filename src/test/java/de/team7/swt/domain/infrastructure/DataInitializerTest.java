package de.team7.swt.domain.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vincent Nadoll
 */
class DataInitializerTest {

    @Test
    void defaultOrder_shouldMatchOrderConstant() {
        DataInitializer initializer = () -> {
        };

        assertEquals(DataInitializer.DEFAULT_ORDER, initializer.getOrder());
    }
}
