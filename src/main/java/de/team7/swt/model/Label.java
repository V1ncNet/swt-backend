package de.team7.swt.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URL;

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
}
