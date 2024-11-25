package fr.uga.miage.m1.my_project.exception.handler;

import fr.uga.miage.m1.my_project.exception.rest.RencontreNotFoundRestException;
import fr.uga.miage.m1.my_project.restapi.exception.RencontreNotFoundResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RencontreNotFoundRestExceptionHandler {

    @ExceptionHandler(RencontreNotFoundRestException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    RencontreNotFoundResponse handle(HttpServletRequest httpServletRequest, RencontreNotFoundRestException e) {
        return RencontreNotFoundResponse.builder()
                .rencontreId(e.getRencontreId())
                .errorMessage(e.getMessage())
                .uri(httpServletRequest.getRequestURI())
                .build();
    }

}
