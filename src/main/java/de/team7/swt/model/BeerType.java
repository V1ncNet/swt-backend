package de.team7.swt.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;


/**
 * Entity class representing a generic type of beer.
 *
 * @author Julian Albrecht
 */
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BeerType extends Product{

    @NonNull
    private String type;
}
