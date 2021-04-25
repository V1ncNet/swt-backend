package de.team7.swt.model;

import de.vinado.spring.data.inmemory.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class BeerType {

    @Id
    private int Id;

    private String type;

    //@OneToMany (mappedBy = "beerType") // TODO in JPA
    private List<Product> productList;

}