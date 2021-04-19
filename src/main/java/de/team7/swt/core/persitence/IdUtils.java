package de.team7.swt.core.persitence;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.lang.Nullable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Collection of functions to interact with entity identifiers.
 *
 * @author Vincent Nadoll
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IdUtils {

    /**
     * Returns the ID of the given entity which must contain an accessor annotated with {@link Id}.
     *
     * @param entity must not be {@literal null}
     * @return the entity's ID
     * @throws IllegalArgumentException in case the entity doesn't contain an applicable id accessor
     */
    @Nullable
    public static Object getId(@NonNull Object entity) {
        try {
            Field idField = getIdField(entity);
            if (null != idField) {
                return idField.get(entity);
            }

            Method idGetter = getIdGetter(entity);
            if (null != idGetter) {
                return idGetter.invoke(entity);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(
                String.format("Couldn't get id accessor from entity [%s]", entity.getClass().getCanonicalName()), e
            );
        }

        throw new IllegalArgumentException(String.format(
            "Entity [%s] has neither field nor method annotated with @Id", entity.getClass().getCanonicalName()
        ));
    }

    static void setId(@NonNull Object entity, @NonNull Object id) {
        try {
            Field idField = getIdField(entity);
            if (null != idField) {
                idField.set(entity, id);
                return;
            }

            Method idGetter = getIdGetter(entity);
            if (null != idGetter) {
                Method idSetter = getIdSetter(entity, id);
                idSetter.invoke(entity, id);
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalArgumentException(
                String.format("Couldn't set id in entity [%s]", entity.getClass().getCanonicalName()), e
            );
        }
    }

    private static Field getIdField(Object entity) {
        return getAccessorDeep(
            entity,
            Class::getDeclaredFields,
            IdUtils::hasIdAnnotation
        );
    }

    private static Method getIdGetter(Object entity) {
        return getAccessorDeep(
            entity,
            Class::getDeclaredMethods,
            IdUtils::hasIdAnnotation
        );
    }

    private static boolean hasIdAnnotation(AccessibleObject accessible) {
        return Objects.nonNull(accessible.getAnnotation(Id.class));
    }

    static <M extends AccessibleObject> M getAccessorDeep(Object entity, Function<Class<?>, M[]> accessor,
                                                          Predicate<M> accessorMatcher) {
        for (Class<?> entityClass = entity.getClass(); null != entityClass; entityClass = entityClass.getSuperclass()) {
            M member = getAccessor(entityClass, accessor, accessorMatcher);
            if (null != member) {
                return member;
            }
        }

        return null;
    }

    static <M extends AccessibleObject> M getAccessor(Class<?> entityClass, Function<Class<?>, M[]> accessors,
                                                      Predicate<M> accessorMatcher) {
        for (M member : accessors.apply(entityClass)) {
            if (accessorMatcher.test(member)) {
                member.setAccessible(true);
                return member;
            }
        }

        return null;
    }

    private static Method getIdSetter(Object entity, Object id) throws NoSuchMethodException {
        Method idGetter = getIdGetter(entity);
        String setterName = idGetter.getName().replaceFirst("^get", "set");
        Method setter = entity.getClass().getMethod(setterName, id.getClass());
        setter.setAccessible(true);
        return setter;
    }
}
