package de.team7.swt.domain.infrastructure.identifier;

import de.team7.swt.domain.shared.Identifier;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.data.util.Streamable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.UUID;

/**
 * {@link org.springframework.core.convert.converter.GenericConverter} which is capable of converting an {@link UUID} or
 * an UUID-{@link String} into a subclass of {@link Identifier}.
 *
 * @author Vincent Nadoll
 */
@Component
public class IdentifierConverter implements ConditionalGenericConverter {

    protected static final TypeDescriptor STRING_TYPE = TypeDescriptor.valueOf(String.class);
    protected static final TypeDescriptor UUID_TYPE = TypeDescriptor.valueOf(UUID.class);
    protected static final TypeDescriptor IDENTIFIER_TYPE = TypeDescriptor.valueOf(Identifier.class);

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return (sourceType.isAssignableTo(STRING_TYPE) || sourceType.isAssignableTo(UUID_TYPE))
            && targetType.isAssignableTo(IDENTIFIER_TYPE);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Streamable.of(
            new ConvertiblePair(String.class, Identifier.class),
            new ConvertiblePair(UUID.class, Identifier.class)
        ).toSet();
    }

    @Override
    @Nullable
    public Identifier convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (null == source) {
            return null;
        }

        Class<?> targetClass = targetType.getType();
        try {
            Constructor<?> constructor = targetClass.getDeclaredConstructor(UUID.class);
            UUID uuid = from(source, sourceType);
            return (Identifier) BeanUtils.instantiateClass(constructor, uuid);
        } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | BeanInstantiationException e) {
            throw new ConversionFailedException(
                TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetClass),
                source, e
            );
        }
    }

    private static UUID from(Object source, TypeDescriptor sourceType) {
        Class<?> sourceClass = sourceType.getType();
        if (sourceClass.isAssignableFrom(UUID.class)) {
            return (UUID) source;
        } else if (sourceClass.isAssignableFrom(String.class)) {
            return UUID.fromString((String) source);
        }

        throw new IllegalStateException("Shouldn't occur!");
    }
}
