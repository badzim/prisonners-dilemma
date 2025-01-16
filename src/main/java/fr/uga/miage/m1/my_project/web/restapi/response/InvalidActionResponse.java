package fr.uga.miage.m1.my_project.web.restapi.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InvalidActionResponse {
    private final String errorMessage;
    private final String uri;
}
