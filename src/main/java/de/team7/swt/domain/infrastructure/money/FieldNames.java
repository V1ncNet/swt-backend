package de.team7.swt.domain.infrastructure.money;

import org.springframework.data.util.Streamable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A collection of JSON field names.
 *
 * @author Vincent Nadoll
 */
class FieldNames implements Streamable<String> {

    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";
    public static final String FORMATTED = "formatted";

    @Override
    public Iterator<String> iterator() {
        return Arrays.asList(AMOUNT, CURRENCY).iterator();
    }

    /**
     * Transforms the {@link #iterator()} results to a list of generic {@link Object}s.
     *
     * @return a list of mandatory field names
     */
    public static List<Object> mandatory() {
        FieldNames names = new FieldNames();
        return names.map(Object.class::cast).toList();
    }

}
