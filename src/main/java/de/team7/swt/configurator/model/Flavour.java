package de.team7.swt.configurator.model;

import de.team7.swt.domain.catalog.Product;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * An entity class representing a beer flavour.
 *
 * @author Julian Albrecht
 */
@Entity
@NoArgsConstructor
public class Flavour extends Product {
}
