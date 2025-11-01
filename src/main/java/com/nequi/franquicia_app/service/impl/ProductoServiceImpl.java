package com.nequi.franquicia_app.service.impl;

import org.springframework.stereotype.Service;

import com.nequi.franquicia_app.dto.request.CrearProductoRequest;
import com.nequi.franquicia_app.dto.request.ModificarStockRequest;
import com.nequi.franquicia_app.dto.response.ProductoMayorStockResponse;
import com.nequi.franquicia_app.exception.FranquiciaNotFoundException;
import com.nequi.franquicia_app.exception.ProductoNotFoundException;
import com.nequi.franquicia_app.exception.SucursalNotFoundException;
import com.nequi.franquicia_app.model.Producto;
import com.nequi.franquicia_app.repository.FranquiciaRepository;
import com.nequi.franquicia_app.repository.ProductoRepository;
import com.nequi.franquicia_app.repository.SucursalRepository;
import com.nequi.franquicia_app.service.ProductoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;
    
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
    
    @Override
    public Flux<ProductoMayorStockResponse> obtenerProductosMayorStockPorSucursal(Long franquiciaId) {
        if (franquiciaId == null) {
            return Flux.error(new IllegalArgumentException("ID de franquicia es requerido"));
        }
        return franquiciaRepository.existsById(franquiciaId)
            .filter(exists -> exists)
            .switchIfEmpty(Mono.error(new FranquiciaNotFoundException(franquiciaId)))
            .flatMapMany(exists -> productoRepository.findByFranquiciaId(franquiciaId)
                .collectMultimap(Producto::getSucursalId, producto -> producto)
                .flatMapMany(productosPorSucursal -> 
                    Flux.fromIterable(productosPorSucursal.entrySet())
                        .flatMap(entry -> {
                            Long branchId = entry.getKey();
                            if (branchId == null) {
                                return Flux.empty();
                            }
                            return Flux.fromIterable(entry.getValue())
                                .reduce((p1, p2) -> p1.getStock() > p2.getStock() ? p1 : p2)
                                .flatMap(producto -> sucursalRepository.findById(branchId)
                                    .map(sucursal -> new ProductoMayorStockResponse(
                                        producto.getId(),
                                        producto.getNombre(),
                                        producto.getStock(),
                                        sucursal.getId(),
                                        sucursal.getNombre()
                                    )));
                        })
                ))
            .doOnNext(response -> log.info("Producto con mayor stock encontrado: {} en sucursal: {}", 
                response.getNombreProducto(), response.getNombreSucursal()))
            .doOnError(e -> log.error("Error obteniendo productos con mayor stock: {}", e.getMessage()));
    }
}