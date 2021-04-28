package de.team7.swt.model;

import de.vinado.spring.data.inmemory.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.net.URI;
import java.util.List;

/**
 * An entity which encapsulates enumerable derivates of a beer bottle and its image location.
 *
 * @author Julian Albrecht
 */
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor

public class Bottle {

    @Id
    private int Id;

    @NonNull
    private URI image;

    //@Enumerated(EnumType.STRING) // TODO JPA?
    private BottleSize size;
    // @Enumerated(EnumType.STRING) // TODO JPA?
    private BottleColor color;

    //@OneToMany (mappedBy = "bottle") // TODO in JPA
    private List<Bottle> bottleList;
}
