package de.team7.swt.domain.infrastructure.identifier;

import de.team7.swt.domain.shared.Entity;
import de.team7.swt.domain.shared.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import java.util.Properties;
import java.util.UUID;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author Vincent Nadoll
 */
class IdentifierGeneratorTest {

    static final UUID ID_VALUE = UUID.fromString("388a79e6-921b-4510-8feb-6157bb7d1ea0");
    static final Id ID = new Id(ID_VALUE);

    private SharedSessionContractImplementor session;
    private IdentifierGenerator generator;

    @BeforeEach
    void setUp() {
        session = mock(SharedSessionContractImplementor.class, Answers.RETURNS_DEEP_STUBS);
        generator = spy(new IdentifierGenerator());

        Properties params = new Properties(1);
        params.setProperty(ENTITY_NAME, "customer");
        generator.configure(mock(Type.class), params, mock(ServiceRegistry.class));
    }

    @Test
    void newCustomer_shouldGenerateNewIdentifier() {
        Customer customer = new Customer();
        when(generator.getIdType(session, customer)).thenAnswer(i -> Id.class);

        Id id = (Id) generator.generate(session, customer);

        assertNotNull(id);
        assertNotNull(id.toString());
    }

    @Test
    void existingIdCustomer_shouldReturnExistingId() {
        Customer customer = new Customer(ID);
        when(generator.getIdType(session, customer)).thenAnswer(i -> Id.class);

        when(session.getEntityPersister(eq("customer"), eq(customer))
            .getIdentifier(eq(customer), eq(session))).thenReturn(ID_VALUE);

        Id id = (Id) generator.generate(session, customer);

        assertNotNull(id);
        assertEquals(ID_VALUE.toString(), id.toString());
    }

    @Test
    void idMissingRequiredOneArgUuidConstructor_shouldThrowException() {
        Person person = new Person();
        when(generator.getIdType(session, person)).thenAnswer(i -> Person.Id.class);

        assertThrows(HibernateException.class, () -> generator.generate(session, person));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    static class Customer extends Entity<Id> {

        private Id id;

        @Override
        public Id getId() {
            return id;
        }
    }

    @Getter
    static class Person {

        private Person.Id id;

        @Getter
        static class Id {

            private final UUID id;

            Id() {
                this.id = UUID.randomUUID();
            }
        }
    }
}
