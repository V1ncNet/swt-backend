package de.team7.swt.domain.web;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * Simple collection wrapper to ensue collection response bodies are always embedded in a JSON root object.
 *
 * @author Vincent Nadoll
 */
@RequiredArgsConstructor
public class CollectionModel {

    @Getter(value = AccessLevel.PRIVATE, onMethod_ = @JsonGetter("_embedded"))
    private final Collection<?> embedded;
}
