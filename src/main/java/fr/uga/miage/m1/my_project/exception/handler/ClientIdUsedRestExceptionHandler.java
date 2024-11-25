package fr.uga.miage.m1.my_project.exception.handler;

import fr.uga.miage.m1.my_project.exception.rest.ClientIdUsedRestException;
import fr.uga.miage.m1.my_project.restapi.exception.ClientIdUsedResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClientIdUsedRestExceptionHandler {

    @ExceptionHandler(ClientIdUsedRestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ClientIdUsedResponse handle(HttpServletRequest httpServletRequest, ClientIdUsedRestException e) {
        return ClientIdUsedResponse.builder()
                .clientId(e.getClientId())
                .errorMessage(e.getMessage())
                .uri(httpServletRequest.getRequestURI())
                .build();
    }

}
