package de.team7.swt.model;

import de.vinado.spring.data.inmemory.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Flavour {

    @Id
    private int id;

    @NonNull
    private String name;

    //@OneToMany (mappedBy = "flavour") // TODO in JPA
    private List<Flavour> flavourList;
}
