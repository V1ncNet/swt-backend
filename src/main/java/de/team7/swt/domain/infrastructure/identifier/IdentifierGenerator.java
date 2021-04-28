package de.team7.swt.domain.infrastructure.identifier;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

/**
 * A Hibernate {@link org.hibernate.id.IdentifierGenerator} to generate new
 * {@link de.team7.swt.domain.shared.Identifier} instances via reflection, but is capable of generating UUID-based IDs
 * for any other type which wraps. The requirement for this generator to work is the availability of a one-arg
 * constructor which consumes UUID type.
 *
 * @author Vincent Nadoll
 * @see de.team7.swt.domain.infrastructure.GlobalIdentifierGeneratorStrategyProvider
 */
public class IdentifierGenerator implements org.hibernate.id.IdentifierGenerator, Configurable {

    private String entityName;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        entityName = params.getProperty(ENTITY_NAME);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Class<?> idType = getIdType(session, object);
        return determineExistingIdentifier(session, object)
            .orElse(newIdentifierViaReflection(idType, UUID.randomUUID()));
    }

    protected Class<?> getIdType(SharedSessionContractImplementor session, Object object) {
        return session.getEntityPersister(entityName, object)
            .getEntityMetamodel()
            .getSessionFactory()
            .getMetamodel()
            .entity(entityName)
            .getIdType()
            .getJavaType();
    }

    protected Optional<Serializable> determineExistingIdentifier(SharedSessionContractImplementor session,
                                                                 Object object) {
        try {
            Class<?> idType = getIdType(session, object);
            EntityPersister entityPersister = session.getEntityPersister(entityName, object);
            return Optional.ofNullable(entityPersister.getIdentifier(object, session))
                .map(Serializable::toString)
                .map(UUID::fromString)
                .map(newIdentifierViaReflection(idType));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private Function<UUID, Serializable> newIdentifierViaReflection(Class<?> idType) {
        return uuid -> newIdentifierViaReflection(idType, uuid);
    }

    protected Serializable newIdentifierViaReflection(Class<?> idType, UUID uuid) {
        try {
            Constructor<?> constructor = ReflectionUtils.accessibleConstructor(idType, UUID.class);
            return (Serializable) BeanUtils.instantiateClass(constructor, uuid);
        } catch (NoSuchMethodException e) {
            throw new HibernateException("Generator requires an one-arg constructor with arg type UUID", e);
        }
    }
}
