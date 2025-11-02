package com.nequi.franquicia_app.exception;

public class ProductoNotFoundException extends RuntimeException {
    
    public ProductoNotFoundException(Long productoId) {
        super("Producto con ID " + productoId + " no encontrado");
    }
}