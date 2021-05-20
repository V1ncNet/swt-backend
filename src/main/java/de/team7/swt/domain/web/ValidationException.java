package de.team7.swt.domain.web;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An {@link RuntimeException} that is thrown in case a requested ressource contains validation which fails.
 *
 * @author Vincent Nadoll
 */
@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
