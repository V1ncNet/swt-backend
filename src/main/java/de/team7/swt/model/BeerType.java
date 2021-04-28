package de.team7.swt.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity class representing a generic type of beer.
 *
 * @author Julian Albrecht
 */
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class BeerType {

    @Id
    private int id;

    @NonNull
    private String type;
}
