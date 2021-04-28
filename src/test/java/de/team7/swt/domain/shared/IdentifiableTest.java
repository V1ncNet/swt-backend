package de.team7.swt.domain.shared;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vincent Nadoll
 */
class IdentifiableTest {

    static final UUID ID_VALUE = UUID.fromString("e70521eb-9238-4dff-8814-8ccd75f0a234");
    static final Id ID = new Id(ID_VALUE);

    @Test
    void nonNullId_shouldNotBeNew() {
        Identifiable<Id> identifiable = () -> ID;
        assertFalse(identifiable.isNew());
    }

    @Test
    void nullId_shouldBeNew() {
        Identifiable<Id> identifiable = () -> null;
        assertTrue(identifiable.isNew());
    }
}
