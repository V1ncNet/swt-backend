package de.team7.swt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.net.URI;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * An entity which encapsulates enumerable derivates of a beer bottle and its image location.
 *
 * @author Julian Albrecht
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Bottle extends Product {

    @NonNull
    private URI image;

    @Enumerated(EnumType.STRING)
    private BottleSize size;

    @Enumerated(EnumType.STRING)
    private BottleColor color;
}
