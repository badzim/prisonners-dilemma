package fr.uga.miage.m1.my_project.exception.handler;

import fr.uga.miage.m1.my_project.core.exception.rest.InvalidActionRestException;
import fr.uga.miage.m1.my_project.restapi.exception.InvalidActionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidActionRestExceptionHandler {
    @ExceptionHandler(InvalidActionRestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    InvalidActionResponse handle(HttpServletRequest httpServletRequest, InvalidActionRestException e) {
        return InvalidActionResponse.builder()
                .errorMessage(e.getMessage())
                .uri(httpServletRequest.getRequestURI())
                .build();
    }
}
