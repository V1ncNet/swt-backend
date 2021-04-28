package de.team7.swt.model;

import de.vinado.spring.data.inmemory.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.net.URL;
import java.util.List;

/**
 * An entity which represents an identifiable label location.
 *
 * @author Julian Albrecht
 */
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Label {

    @Id
    private int id;

    @NonNull
    private URL image;

    //@OneToMany (mappedBy = "label") // TODO in JPA
    private List<Label> labelList;
}