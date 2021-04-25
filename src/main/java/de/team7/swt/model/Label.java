package de.team7.swt.model;

import de.vinado.spring.data.inmemory.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.net.URL;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Label {

    @Id
    private int id;

    private URL image;

    //@OneToMany (mappedBy = "label") // TODO in JPA
    private List<Label> labelList;
}
