package de.team7.swt.model;

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

    @NonNull
    private String name;
public class Ingredient extends Product {
}
