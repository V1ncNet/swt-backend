package de.team7.swt.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * An entity class representing a beer flavour.
 *
 * @author Julian Albrecht
 */
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Flavour {

    @Id
    private int id;

    @NonNull
    private String name;
}
