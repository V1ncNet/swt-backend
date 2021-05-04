package de.team7.swt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;


/**
 * An entity class representing a beer flavour.
 *
 * @author Julian Albrecht
 */
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Flavour extends Product{

    @NonNull
    private String name;
}
