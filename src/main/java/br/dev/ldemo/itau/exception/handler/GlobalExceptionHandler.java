package br.dev.ldemo.itau.exception.handler;

import br.dev.ldemo.itau.exception.ApiErrorResponse;
import br.dev.ldemo.itau.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de recurso não encontrado.
     *
     * @param ex Exceção lançada
     * @param request Requisição HTTP
     * @return Resposta de erro
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            final ResourceNotFoundException ex, final HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            final Exception e,
            final HttpStatus status,
            final String message,
            final HttpServletRequest request) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
        log.error("Exception handled: {}", errorResponse, e);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
