package com.nequi.franquicia_app.controller;

import com.nequi.franquicia_app.dto.request.CrearProductoRequest;
import com.nequi.franquicia_app.exception.ProductoNotFoundException;
import com.nequi.franquicia_app.exception.SucursalNotFoundException;
import com.nequi.franquicia_app.model.Producto;
import com.nequi.franquicia_app.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class ProductoController {
    
    private final ProductoService productoService;
    
    @PostMapping("/{sucursalId}/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Producto> crearProducto(
            @PathVariable Long sucursalId,
            @RequestBody CrearProductoRequest request) {
        return productoService.crearProducto(sucursalId, request)
            .onErrorMap(IllegalArgumentException.class, 
                ex -> new RuntimeException("Datos inválidos: " + ex.getMessage()))
            .onErrorMap(SucursalNotFoundException.class,
                ex -> new RuntimeException(ex.getMessage()));
    }
    
    @DeleteMapping("/productos/{productoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarProducto(@PathVariable Long productoId) {
        return productoService.eliminarProducto(productoId)
            .onErrorMap(ProductoNotFoundException.class,
                ex -> new RuntimeException(ex.getMessage()));
    }
}