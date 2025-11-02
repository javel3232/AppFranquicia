package com.nequi.franquicia_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_KEY = "error";

    @ExceptionHandler(FranquiciaNotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleFranquiciaNotFound(FranquiciaNotFoundException ex) {
        return Mono.just(ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of(ERROR_KEY, ex.getMessage())));
    }

    @ExceptionHandler(SucursalNotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleSucursalNotFound(SucursalNotFoundException ex) {
        return Mono.just(ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of(ERROR_KEY, ex.getMessage())));
    }

    @ExceptionHandler(ProductoNotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleProductoNotFound(ProductoNotFoundException ex) {
        return Mono.just(ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of(ERROR_KEY, ex.getMessage())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleIllegalArgument(IllegalArgumentException ex) {
        return Mono.just(ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of(ERROR_KEY, "Datos inválidos: " + ex.getMessage())));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleRuntimeException(RuntimeException ex) {
        if (ex.getMessage().contains("not found") || ex.getMessage().contains("no encontrada")) {
            return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(ERROR_KEY, ex.getMessage())));
        }
        
        return Mono.just(ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of(ERROR_KEY, ex.getMessage())));
    }
}