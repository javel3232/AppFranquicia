package com.nequi.franquicia_app.service;

import com.nequi.franquicia_app.dto.request.CrearProductoRequest;
import com.nequi.franquicia_app.exception.ProductoNotFoundException;
import com.nequi.franquicia_app.exception.SucursalNotFoundException;
import com.nequi.franquicia_app.model.Producto;
import com.nequi.franquicia_app.repository.ProductoRepository;
import com.nequi.franquicia_app.repository.SucursalRepository;
import com.nequi.franquicia_app.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void crearProducto_DeberiaCrearProducto_CuandoDatosValidos() {
        Long sucursalId = 1L;
        CrearProductoRequest request = new CrearProductoRequest("Big Mac", 50);
        Producto producto = new Producto(1L, "Big Mac", 50, sucursalId);
        
        when(sucursalRepository.existsById(sucursalId)).thenReturn(Mono.just(true));
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(producto));
        StepVerifier.create(productoService.crearProducto(sucursalId, request))
                .expectNext(producto)
                .verifyComplete();
    }

    @Test
    void crearProducto_DeberiaFallar_CuandoSucursalNoExiste() {
        Long sucursalId = 999L;
        CrearProductoRequest request = new CrearProductoRequest("Big Mac", 50);
        
        when(sucursalRepository.existsById(sucursalId)).thenReturn(Mono.just(false));
        StepVerifier.create(productoService.crearProducto(sucursalId, request))
                .expectError(SucursalNotFoundException.class)
                .verify();
    }

    @Test
    void eliminarProducto_DeberiaFallar_CuandoProductoNoExiste() {
        Long productoId = 999L;
        when(productoRepository.existsById(productoId)).thenReturn(Mono.just(false));
        StepVerifier.create(productoService.eliminarProducto(productoId))
                .expectError(ProductoNotFoundException.class)
                .verify();
    }

    @Test
    void eliminarProducto_DeberiaEliminar_CuandoProductoExiste() {
        Long productoId = 1L;
        when(productoRepository.existsById(productoId)).thenReturn(Mono.just(true));
        when(productoRepository.deleteById(productoId)).thenReturn(Mono.empty());
        StepVerifier.create(productoService.eliminarProducto(productoId))
                .verifyComplete();
    }
}