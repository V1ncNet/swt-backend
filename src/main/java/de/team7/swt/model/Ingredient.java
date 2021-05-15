package de.team7.swt.model;

import de.team7.swt.domain.catalog.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;


@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ingredient extends Product {

    @NonNull
    private String name;
}
