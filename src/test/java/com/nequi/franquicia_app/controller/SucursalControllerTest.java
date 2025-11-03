package com.nequi.franquicia_app.controller;

import com.nequi.franquicia_app.dto.response.SucursalConProductosResponse;
import com.nequi.franquicia_app.exception.SucursalNotFoundException;
import com.nequi.franquicia_app.model.Producto;
import com.nequi.franquicia_app.service.SucursalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(SucursalController.class)
class SucursalControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SucursalService sucursalService;

    @Test
    void obtenerSucursalConProductos_DeberiaRetornarSucursalConProductos() {
        // Given
        Long sucursalId = 1L;
        SucursalConProductosResponse response = new SucursalConProductosResponse(
            1L, "Sucursal Centro", 1L,
            Arrays.asList(
                new Producto(1L, "Producto 1", 10, 1L),
                new Producto(2L, "Producto 2", 20, 1L)
            )
        );

        when(sucursalService.obtenerSucursalConProductos(sucursalId))
            .thenReturn(Mono.just(response));

        // When & Then
        webTestClient.get()
            .uri("/api/franquicias/sucursales/{sucursalId}/productos", sucursalId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(SucursalConProductosResponse.class)
            .value(result -> {
                assert result.getId().equals(1L);
                assert result.getNombre().equals("Sucursal Centro");
                assert result.getProductos().size() == 2;
            });
    }

    @Test
    void obtenerSucursalConProductos_SucursalNoExiste_DeberiaRetornar404() {
        // Given
        Long sucursalId = 999L;
        when(sucursalService.obtenerSucursalConProductos(sucursalId))
            .thenReturn(Mono.error(new SucursalNotFoundException(sucursalId)));

        // When & Then
        webTestClient.get()
            .uri("/api/franquicias/sucursales/{sucursalId}/productos", sucursalId)
            .exchange()
            .expectStatus().isNotFound();
    }
}