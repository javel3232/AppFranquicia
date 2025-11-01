package com.nequi.franquicia_app.service.impl;

import org.springframework.stereotype.Service;

import com.nequi.franquicia_app.dto.request.CrearProductoRequest;
import com.nequi.franquicia_app.dto.request.ModificarStockRequest;
import com.nequi.franquicia_app.exception.ProductoNotFoundException;
import com.nequi.franquicia_app.exception.SucursalNotFoundException;
import com.nequi.franquicia_app.model.Producto;
import com.nequi.franquicia_app.repository.ProductoRepository;
import com.nequi.franquicia_app.repository.SucursalRepository;
import com.nequi.franquicia_app.service.ProductoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    
    @Override
    public Mono<Producto> crearProducto(Long sucursalId, CrearProductoRequest request) {
        if (sucursalId == null) {
            return Mono.error(new IllegalArgumentException("ID de sucursal es requerido"));
        }
        return Mono.just(request)
            .filter(req -> req.getNombre() != null && !req.getNombre().trim().isEmpty())
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Nombre es requerido")))
            .filter(req -> req.getStock() != null && req.getStock() >= 0)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Stock debe ser mayor o igual a 0")))
            .flatMap(req -> sucursalRepository.existsById(sucursalId)
                .filter(exists -> exists)
                .switchIfEmpty(Mono.error(new SucursalNotFoundException(sucursalId)))
                .map(exists -> new Producto(null, req.getNombre().trim(), req.getStock(), sucursalId)))
            .flatMap(productoRepository::save)
            .doOnSuccess(p -> log.info("Producto creado: {} para sucursal ID: {}", p.getNombre(), sucursalId))
            .doOnError(e -> log.error("Error creando producto: {}", e.getMessage()));
    }
    
    @Override
    public Mono<Void> eliminarProducto(Long productoId) {
        if (productoId == null) {
            return Mono.error(new IllegalArgumentException("ID de producto es requerido"));
        }
        return productoRepository.existsById(productoId)
            .filter(exists -> exists)
            .switchIfEmpty(Mono.error(new ProductoNotFoundException(productoId)))
            .flatMap(exists -> productoRepository.deleteById(productoId))
            .doOnSuccess(v -> log.info("Producto eliminado con ID: {}", productoId))
            .doOnError(e -> log.error("Error eliminando producto: {}", e.getMessage()));
    }
    
    @Override
    public Mono<Producto> modificarStock(Long productoId, ModificarStockRequest request) {
        if (productoId == null) {
            return Mono.error(new IllegalArgumentException("ID de producto es requerido"));
        }
        return Mono.just(request)
            .filter(req -> req.getNuevoStock() != null && req.getNuevoStock() >= 0)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Stock debe ser mayor o igual a 0")))
            .flatMap(req -> productoRepository.findById(productoId)
                .switchIfEmpty(Mono.error(new ProductoNotFoundException(productoId)))
                .map(producto -> {
                    producto.setStock(req.getNuevoStock());
                    return producto;
                }))
            .flatMap(productoRepository::save)
            .doOnSuccess(p -> log.info("Stock actualizado para producto ID: {} - Nuevo stock: {}", productoId, p.getStock()))
            .doOnError(e -> log.error("Error modificando stock: {}", e.getMessage()));
    }
}