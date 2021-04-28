package de.team7.swt.domain.shared;

import lombok.NonNull;

import java.util.UUID;

/**
 * @author Vincent Nadoll
 */
public class Id extends Identifier {
    public Id(@NonNull UUID id) {
        super(id);
    }
}
