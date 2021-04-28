package de.team7.swt.domain.infrastructure.identifier;

import de.team7.swt.domain.shared.Id;
import de.team7.swt.domain.shared.Identifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.team7.swt.domain.infrastructure.identifier.IdentifierConverter.IDENTIFIER_TYPE;
import static de.team7.swt.domain.infrastructure.identifier.IdentifierConverter.STRING_TYPE;
import static de.team7.swt.domain.infrastructure.identifier.IdentifierConverter.UUID_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vincent Nadoll
 */
class IdentifierConverterTest {

    private static final UUID ID_VALUE = UUID.fromString("06668f94-c5c0-4999-94a6-931a9d47dbfc");
    private static final TypeDescriptor OBJECT_DESCRIPTOR = TypeDescriptor.valueOf(Object.class);
    private static final TypeDescriptor ID_DESCRIPTOR = TypeDescriptor.valueOf(Id.class);

    private IdentifierConverter converter;

    @BeforeEach
    void setUp() {
        converter = new IdentifierConverter();
    }

    @Test
    void uuidCompatibleSourceTypes_shouldBeConvertible() {
        Stream.of(STRING_TYPE, UUID_TYPE)
            .map(sourceType -> converter.matches(sourceType, IDENTIFIER_TYPE))
            .forEach(Assertions::assertTrue);
    }

    @Test
    void uuidIncompatibleSourceType_shouldNotBeConvertible() {
        assertFalse(converter.matches(OBJECT_DESCRIPTOR, IDENTIFIER_TYPE));
    }

    @Test
    void identifierCompatibleTargetType_shouldBeConvertible() {
        assertTrue(converter.matches(STRING_TYPE, ID_DESCRIPTOR));
    }

    @Test
    void identifierIncompatibleTargetType_shouldNotBeConvertible() {
        assertFalse(converter.matches(STRING_TYPE, OBJECT_DESCRIPTOR));
    }

    @Test
    void convertNullArgument_shouldReturnNull() {
        assertNull(converter.convert(null, UUID_TYPE, IDENTIFIER_TYPE));
        assertNull(converter.convert(null, STRING_TYPE, IDENTIFIER_TYPE));
    }

    @Test
    void convertToAbstractIdentifier_shouldThrowException() {
        assertThrows(ConversionFailedException.class, () -> converter.convert(ID_VALUE, UUID_TYPE, IDENTIFIER_TYPE));
    }

    @Test
    void uuid_shouldBeConverted() {
        Identifier id = converter.convert(ID_VALUE, UUID_TYPE, ID_DESCRIPTOR);

        assertNotNull(id);
        assertEquals(ID_VALUE.toString(), id.toString());
    }

    @Test
    void uuidString_shouldBeConverted() {
        String source = ID_VALUE.toString();
        Identifier id = converter.convert(source, STRING_TYPE, ID_DESCRIPTOR);

        assertNotNull(id);
        assertEquals(source, id.toString());
    }

    @Test
    void convertibleTypes_shouldContain() {
        Set<Class<?>> provided = converter.getConvertibleTypes().stream()
            .map(GenericConverter.ConvertiblePair::getSourceType)
            .collect(Collectors.toSet());
        Set<Class<?>> configured = Stream.of(STRING_TYPE, UUID_TYPE)
            .map(TypeDescriptor::getType)
            .collect(Collectors.toSet());

        assertTrue(provided.containsAll(configured));
    }
}
