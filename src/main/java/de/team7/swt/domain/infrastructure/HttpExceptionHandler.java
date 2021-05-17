package de.team7.swt.domain.infrastructure;

import de.team7.swt.checkout.model.OrderCompletionFailure;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Middleware for catching exceptions and transforming them into meaningful HTTP responses.
 *
 * @author Vincent Nadoll
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class HttpExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Catches the {@link OrderCompletionFailure} and writes its underlying
     * {@link de.team7.swt.checkout.application.OrderCompletionReport} to the response body.
     *
     * @param ex      caught {@link OrderCompletionFailure}
     * @param request current {@link WebRequest}
     * @return 419 Conflict - meaningful failed {@link de.team7.swt.checkout.application.OrderCompletionReport}
     */
    @ExceptionHandler(OrderCompletionFailure.class)
    protected ResponseEntity<Object> handleFailedOrderCompletion(OrderCompletionFailure ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.CONFLICT;
        return handleExceptionInternal(ex, ex.getReport(), headers, status, request);
    }
}
