package de.team7.swt.configurator.model;

import de.team7.swt.domain.quantity.Metric;
import de.team7.swt.domain.quantity.Quantity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A collection of common beer bottle sizes and their associated quantities.
 *
 * @author Julian Albrecht
 * @see Bottle
 */
@Getter
@RequiredArgsConstructor
public enum BottleSize {
    SMALL(Quantity.of(0.33, Metric.LITER)),
    NORMAL(Quantity.of(0.5, Metric.LITER)),
    ;

    private final Quantity quantity;
}
