package de.team7.swt.core.persitence;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An in-memory implementation of Spring's {@literal CrudRepository} which stores entities in a {@literal HashMap}.
 *
 * @param <T>  the type of the entity to handle
 * @param <ID> the type of the entity's identifier
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class InMemoryRepository<T, ID> implements ListSupportingCrudRepository<T, ID> {

    public static final String ENTITY_MUST_NOT_BE_NULL = "Entity must not be null";
    public static final String ENTITIES_MUST_NOT_BE_NULL = "Entities must not be null";
    public static final String ID_MUST_NOT_BE_NULL = "ID must not be null";

    protected final Map<ID, T> store = new HashMap<>();

    @NonNull
    private final Supplier<ID> nextIdSupplier;

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);
        ID id = getId(entity);
        if (null == id) {
            id = nextIdSupplier.get();
            setId(entity, id);
        }

        store.put(id, entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    private ID getId(Object entity) {
        try {
            Field idField = getIdField(entity);
            if (null != idField) {
                return (ID) idField.get(entity);
            }

            Method idGetter = getIdGetter(entity);
            if (null != idGetter) {
                return (ID) idGetter.invoke(entity);
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

    private void setId(Object entity, ID id) {
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
            throw new RuntimeException(
                String.format("Couldn't set id in entity [%s]", entity.getClass().getCanonicalName()), e
            );
        }
    }

    private Field getIdField(Object entity) {
        return getAccessorDeep(
            entity,
            Class::getDeclaredFields,
            this::hasIdAnnotation
        );
    }

    private Method getIdGetter(Object entity) {
        return getAccessorDeep(
            entity,
            Class::getDeclaredMethods,
            this::hasIdAnnotation
        );
    }

    private boolean hasIdAnnotation(AccessibleObject accessible) {
        return Objects.nonNull(accessible.getAnnotation(Id.class));
    }

    private <M extends AccessibleObject> M getAccessorDeep(Object entity, Function<Class<?>, M[]> accessor,
                                                           Predicate<M> accessorMatcher) {
        for (Class<?> entityClass = entity.getClass(); null != entityClass; entityClass = entityClass.getSuperclass()) {
            M member = getAccessor(entityClass, accessor, accessorMatcher);
            if (null != member) {
                return member;
            }
        }

        return null;
    }

    private <M extends AccessibleObject> M getAccessor(Class<?> entityClass, Function<Class<?>, M[]> accessors,
                                                       Predicate<M> accessorMatcher) {
        for (M member : accessors.apply(entityClass)) {
            if (accessorMatcher.test(member)) {
                member.setAccessible(true);
                return member;
            }
        }

        return null;
    }

    private Method getIdSetter(Object entity, ID id) throws NoSuchMethodException {
        Method idGetter = getIdGetter(entity);
        String setterName = idGetter.getName().replaceFirst("^get", "set");
        Method setter = entity.getClass().getMethod(setterName, id.getClass());
        setter.setAccessible(true);
        return setter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        Assert.notNull(entities, ENTITIES_MUST_NOT_BE_NULL);
        return stream(entities)
            .map(this::save)
            .collect(Collectors.toList());
    }

    private static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<T> findById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        return Optional.ofNullable(store.get(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        return store.containsKey(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        Assert.notNull(ids, "IDs must not be null");
        return stream(ids)
            .map(findOrThrow(id -> new NoResultException(String.format("No entity w/ id [%s] exists", id))))
            .collect(Collectors.toList());
    }

    private Function<ID, T> findOrThrow(Function<ID, RuntimeException> exception) {
        return id -> findById(id).orElseThrow(() -> exception.apply(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() {
        return store.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        findById(id).ifPresentOrElse(this::delete, () -> {
            throw new NoResultException(String.format("No entity w/ id [%s] exists", id));
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(T entity) {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);

        ID id = getId(entity);
        if (null == id) {
            return;
        }

        Optional<T> existing = findById(id);
        if (existing.isEmpty()) {
            return;
        }

        store.remove(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        Assert.notNull(entities, ENTITIES_MUST_NOT_BE_NULL);
        stream(entities).forEach(this::delete);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll() {
        store.clear();
    }

    /**
     * Returns all instances of the type {@code T} with the given property and its value.
     *
     * @param propertyName must not be {@literal null}
     * @param value        might be {@literal null}
     * @param <F>          the type of the property's value
     * @return a subset of all entities matching the provided property name and its value
     */
    protected <F> Stream<T> findAllBy(String propertyName, F value) {
        Assert.notNull(propertyName, "Property name must not be null");
        return store.values().stream()
            .filter(by(propertyName, value));
    }

    private <F> Predicate<T> by(String property, F value) {
        return entity -> {
            try {
                return has(property, value, entity);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(
                    String.format(
                        "Couldn't get property [%s] from [%s]",
                        property,
                        entity.getClass().getCanonicalName()
                    ), e
                );
            }
        };
    }

    private <F> boolean has(String property, F value, T entity) throws NoSuchFieldException, IllegalAccessException {
        Field field = getAccessorDeep(entity, Class::getDeclaredFields, contains(property));
        if (null == field) {
            throw new NoSuchFieldException(
                String.format("Entity [%s] has no property [%s]", entity.getClass().getCanonicalName(), property)
            );
        }

        return Objects.equals(value, field.get(entity));
    }

    private static Predicate<Field> contains(String property) {
        return field -> Objects.equals(field.getName(), property);
    }

    /**
     * Returns a sole entity instance of the type {@code T} with the given property name and its value.
     *
     * @param propertyName must not be {@literal null}
     * @param value        might be {@literal null}
     * @param <F>          the type of the property's value
     * @return at most one entity matching the provided property name and its value
     * @throws NonUniqueResultException in case the repository contains more than one entity which matches the given
     *                                  query
     */
    protected <F> Optional<T> findUniqueBy(String propertyName, F value) throws NonUniqueResultException {
        List<T> resultList = findAllBy(propertyName, value).collect(Collectors.toList());

        switch (resultList.size()) {
            case 0:
                return Optional.empty();
            case 1:
                T unique = resultList.get(0);
                return Optional.ofNullable(unique);
            default:
                throw new NonUniqueResultException(String.format(
                    "Property [%s] is not a unique field in entity [%s]",
                    propertyName,
                    resultList.get(0).getClass().getCanonicalName()
                ));
        }
    }
}
