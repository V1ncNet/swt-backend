package de.team7.swt.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

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
    private int id;

    @NonNull
    private URI image;

    @Enumerated(EnumType.STRING)
    private BottleSize size;

    @Enumerated(EnumType.STRING)
    private BottleColor color;
}
