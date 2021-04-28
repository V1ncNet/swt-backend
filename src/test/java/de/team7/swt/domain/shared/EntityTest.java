package de.team7.swt.domain.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Vincent Nadoll
 */
class EntityTest {

    static final UUID A_ID_VALUE = UUID.fromString("4fdedc58-1be8-4fd4-9aaa-03f7f5af9d1d");
    static final Id A_CUSTOMER_ID = new Id(A_ID_VALUE);

    static final UUID B_ID_VALUE = UUID.fromString("2ea9e7c3-66d9-4e36-8588-78a177c487c5");
    static final Id B_CUSTOMER_ID = new Id(B_ID_VALUE);

    Entity<Id> a;
    Entity<Id> b;

    @BeforeEach
    void setUp() {
        a = new Customer(A_CUSTOMER_ID);
        b = new Customer(B_CUSTOMER_ID);
    }

    @Test
    void equals_shouldCompareIds() {
        Entity<Id> other = new Customer(A_CUSTOMER_ID);

        assertEquals(a, a);
        assertEquals(a, other);
        assertNotEquals(b, other);
        assertNotEquals(a, new Object());
        assertNotEquals(a, null);
        assertNotEquals(null, a);
    }

    @Test
    void hashCode_shouldCompareIds() {
        assertEquals(A_CUSTOMER_ID.hashCode(), a.hashCode());
        assertNotEquals(A_CUSTOMER_ID.hashCode(), b.hashCode());
    }

    @Getter
    @AllArgsConstructor
    static class Customer extends Entity<Id> {
        private final Id id;
    }
}
