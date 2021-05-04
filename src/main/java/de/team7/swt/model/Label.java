package de.team7.swt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.net.URL;
import javax.persistence.Entity;

/**
 * An entity which represents an identifiable label location.
 *
 * @author Julian Albrecht
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Label extends Product {

    @NonNull
    private URL image;
}
