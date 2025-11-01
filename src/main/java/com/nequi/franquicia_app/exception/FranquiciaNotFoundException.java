package com.nequi.franquicia_app.exception;

public class FranquiciaNotFoundException extends RuntimeException {
    
    public FranquiciaNotFoundException(Long franquiciaId) {
        super("Franquicia con ID " + franquiciaId + " no encontrada");
    }
}