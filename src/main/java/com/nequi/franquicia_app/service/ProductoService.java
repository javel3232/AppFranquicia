package com.nequi.franquicia_app.service;

import com.nequi.franquicia_app.dto.request.CrearProductoRequest;
import com.nequi.franquicia_app.dto.request.ModificarStockRequest;
import com.nequi.franquicia_app.dto.response.ProductoMayorStockResponse;
import com.nequi.franquicia_app.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {
    
    Mono<Producto> crearProducto(Long sucursalId, CrearProductoRequest request);
    Mono<Void> eliminarProducto(Long productoId);
    Mono<Producto> modificarStock(Long productoId, ModificarStockRequest request);
    Flux<ProductoMayorStockResponse> obtenerProductosMayorStockPorSucursal(Long franquiciaId);
}