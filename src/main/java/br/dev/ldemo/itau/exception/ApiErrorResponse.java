package br.dev.ldemo.itau.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ApiErrorResponse {

    private final int status;
    private final String error;
    private final String message;
    private final String path;
}
