package com.nequi.franquicia_app.exception;

public class SucursalNotFoundException extends RuntimeException {
    
    public SucursalNotFoundException(Long sucursalId) {
        super("Sucursal con ID " + sucursalId + " no encontrada");
    }
}