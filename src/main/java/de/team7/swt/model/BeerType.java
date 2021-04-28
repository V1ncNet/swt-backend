package de.team7.swt.model;

import de.vinado.spring.data.inmemory.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

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
    private int Id;

    @NonNull
    private String type;

    //@OneToMany (mappedBy = "beerType") // TODO in JPA
    private List<BeerType> beerTypeList;

}
