package de.team7.swt.domain.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Vincent Nadoll
 */
class IdentifierTest {

    static final UUID A_VALUE = UUID.fromString("5297595b-9fd6-415f-8ed4-0b84ce730ec1");
    static final UUID B_VALUE = UUID.fromString("50f31282-7f07-41e2-bc8f-27aec6e72caa");

    Identifier a;
    Identifier b;

    @BeforeEach
    void setUp() {
        a = new Id(A_VALUE);
        b = new Id(B_VALUE);
    }

    @Test
    void initializeNullArguments_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Id(null));
    }

    @Test
    void toString_shouldBeUuidCompatible() {
        assertDoesNotThrow(() -> UUID.fromString(a.toString()));
    }

    @Test
    void equals_shouldCompareValue() {
        Id other = new Id(A_VALUE);

        assertEquals(a, other);
        assertNotEquals(b, other);
    }

    @Test
    void hashCode_shouldHashValue() {
        assertEquals(A_VALUE.hashCode(), a.hashCode());
        assertNotEquals(A_VALUE.hashCode(), b.hashCode());
    }

    @Test
    void compareTo_shouldCompareValue() {
        Identifier other = new Id(A_VALUE);

        assertEquals(0, a.compareTo(other));
        assertNotEquals(0, b.compareTo(other));
    }
}
