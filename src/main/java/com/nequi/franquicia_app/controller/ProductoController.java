package com.nequi.franquicia_app.controller;

import com.nequi.franquicia_app.dto.request.ActualizarProductoRequest;
import com.nequi.franquicia_app.dto.request.CrearProductoRequest;
import com.nequi.franquicia_app.dto.request.ModificarStockRequest;
import com.nequi.franquicia_app.dto.response.ProductoMayorStockResponse;
import com.nequi.franquicia_app.exception.FranquiciaNotFoundException;
import com.nequi.franquicia_app.exception.ProductoNotFoundException;
import com.nequi.franquicia_app.exception.SucursalNotFoundException;
import com.nequi.franquicia_app.model.Producto;
import com.nequi.franquicia_app.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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
        return productoService.crearProducto(sucursalId, request);
    }
    
    @DeleteMapping("/productos/{productoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarProducto(@PathVariable Long productoId) {
        return productoService.eliminarProducto(productoId);
    }
    
    @PutMapping("/productos/{productoId}/stock")
    public Mono<Producto> modificarStock(
            @PathVariable Long productoId,
            @RequestBody ModificarStockRequest request) {
        return productoService.modificarStock(productoId, request);
    }
    
    @PutMapping("/productos/{productoId}/nombre")
    public Mono<Producto> actualizarNombre(
            @PathVariable Long productoId,
            @RequestBody ActualizarProductoRequest request) {
        return productoService.actualizarNombre(productoId, request);
    }
    
    @GetMapping("/franquicias/{franquiciaId}/productos/mayor-stock")
    public Flux<ProductoMayorStockResponse> obtenerProductosMayorStockPorSucursal(
            @PathVariable Long franquiciaId) {
        return productoService.obtenerProductosMayorStockPorSucursal(franquiciaId);
    }
}