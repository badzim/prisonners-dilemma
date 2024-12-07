package fr.uga.miage.m1.my_project.restapi.exception;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientIdUsedResponse {
    private final String clientId;
    private final String errorMessage;
    private final String uri;
}
