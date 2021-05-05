package de.team7.swt.domain.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * Simple collection wrapper to ensue collection response bodies are always embedded in a JSON root object.
 *
 * @author Vincent Nadoll
 */
public class CollectionModel<T> implements Streamable<T> {

    private final Collection<T> content;

    protected CollectionModel() {
        this(new ArrayList<T>());
    }

    private CollectionModel(Iterable<T> content) {
        Assert.notNull(content, "Content must not be null");
        this.content = new ArrayList<>();

        for (T element : content) {
            this.content.add(element);
        }
    }

    public static <T> CollectionModel<T> of(Iterable<T> content) {
        return new CollectionModel<>(content);
    }

    @JsonProperty("_embedded")
    public Collection<T> getContent() {
        return Collections.unmodifiableCollection(content);
    }

    @Override
    @JsonIgnore
    public boolean isEmpty() {
        return Streamable.super.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof CollectionModel)) {
            return false;
        } else {
            CollectionModel<?> that = (CollectionModel<?>) o;
            return Objects.equals(content, that.content);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(content).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("content", getContent())
            .toString();
    }
}
