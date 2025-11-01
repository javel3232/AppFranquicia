package com.nequi.franquicia_app.controller;

import com.nequi.franquicia_app.dto.request.CrearFranquiciaRequest;
import com.nequi.franquicia_app.model.Franquicia;
import com.nequi.franquicia_app.service.FranquiciaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(FranquiciaController.class)
class FranquiciaControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FranquiciaService franquiciaService;

    @Test
    void createFranchise_ShouldReturn201_WhenValidData() {
        var franchise = new Franquicia(1L, "McDonald's");
        when(franquiciaService.crearFranquicia(any(CrearFranquiciaRequest.class)))
                .thenReturn(Mono.just(franchise));

        var mediaType = MediaType.APPLICATION_JSON;
        Assertions.assertNotNull(mediaType);
        webTestClient.post()
                .uri("/api/franquicias")
                .contentType(mediaType)
                .bodyValue("{\"nombre\": \"McDonald's\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombre").isEqualTo("McDonald's");
    }

    @Test
    void getAllFranchises_ShouldReturn200() {
        var franchise1 = new Franquicia(1L, "McDonald's");
        var franchise2 = new Franquicia(2L, "Burger King");
        when(franquiciaService.obtenerTodasLasFranquicias())
                .thenReturn(Flux.just(franchise1, franchise2));
        webTestClient.get()
                .uri("/api/franquicias")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Franquicia.class)
                .hasSize(2);
    }

    @Test
    void getFranchiseById_ShouldReturn200_WhenExists() {
        var franchise = new Franquicia(1L, "McDonald's");
        when(franquiciaService.obtenerFranquiciaPorId(1L))
                .thenReturn(Mono.just(franchise));
        webTestClient.get()
                .uri("/api/franquicias/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombre").isEqualTo("McDonald's");
    }
}