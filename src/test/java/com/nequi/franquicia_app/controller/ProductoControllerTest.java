package com.nequi.franquicia_app.controller;

import com.nequi.franquicia_app.dto.request.CrearProductoRequest;
import com.nequi.franquicia_app.model.Producto;
import com.nequi.franquicia_app.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductoService productoService;

    @Test
    void createProduct_ShouldReturn201_WhenValidData() {
        var product = new Producto(1L, "Big Mac", 50, 1L);
        when(productoService.crearProducto(eq(1L), any(CrearProductoRequest.class)))
                .thenReturn(Mono.just(product));

        var mediaType = MediaType.APPLICATION_JSON;
        Assertions.assertNotNull(mediaType);
        webTestClient.post()
                .uri("/api/sucursales/1/productos")
                .contentType(mediaType)
                .bodyValue("{\"nombre\": \"Big Mac\", \"stock\": 50}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombre").isEqualTo("Big Mac")
                .jsonPath("$.stock").isEqualTo(50);
    }

    @Test
    void deleteProduct_ShouldReturn204_WhenProductExists() {
        when(productoService.eliminarProducto(1L)).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri("/api/sucursales/productos/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void updateStock_ShouldReturn200_WhenValidData() {
        var updatedProduct = new Producto(1L, "Big Mac", 75, 1L);
        when(productoService.modificarStock(eq(1L), any()))
                .thenReturn(Mono.just(updatedProduct));
        var mediaType = MediaType.APPLICATION_JSON;
        Assertions.assertNotNull(mediaType);
        
        webTestClient.put()
                .uri("/api/sucursales/productos/1/stock")
                .contentType(mediaType)
                .bodyValue("{\"nuevoStock\": 75}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.stock").isEqualTo(75);
    }
}