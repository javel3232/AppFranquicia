package com.nequi.franquicia_app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nequi.franquicia_app.dto.request.CrearFranquiciaRequest;
import com.nequi.franquicia_app.exception.FranquiciaNotFoundException;
import com.nequi.franquicia_app.model.Franquicia;
import com.nequi.franquicia_app.repository.FranquiciaRepository;
import com.nequi.franquicia_app.service.impl.FranquiciaServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FranquiciaServiceTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private FranquiciaServiceImpl franquiciaService;

    @Test
    void crearFranquicia_DeberiaCrearFranquicia_CuandoDatosValidos() {
        // Given
        CrearFranquiciaRequest request = new CrearFranquiciaRequest("McDonald's");
        Franquicia franquicia = new Franquicia(1L, "McDonald's");
        
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

        // When & Then
        StepVerifier.create(franquiciaService.crearFranquicia(request))
                .expectNext(franquicia)
                .verifyComplete();
    }

    @Test
    void crearFranquicia_DeberiaFallar_CuandoNombreVacio() {
        // Given
        CrearFranquiciaRequest request = new CrearFranquiciaRequest("");

        // When & Then
        StepVerifier.create(franquiciaService.crearFranquicia(request))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void obtenerTodasLasFranquicias_DeberiaRetornarLista() {
        // Given
        Franquicia franquicia1 = new Franquicia(1L, "McDonald's");
        Franquicia franquicia2 = new Franquicia(2L, "Burger King");
        
        when(franquiciaRepository.findAll()).thenReturn(Flux.just(franquicia1, franquicia2));

        // When & Then
        StepVerifier.create(franquiciaService.obtenerTodasLasFranquicias())
                .expectNext(franquicia1)
                .expectNext(franquicia2)
                .verifyComplete();
    }

    @Test
    void obtenerFranquiciaPorId_DeberiaFallar_CuandoNoExiste() {
        // Given
        Long franquiciaId = 999L;
        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(franquiciaService.obtenerFranquiciaPorId(franquiciaId))
                .expectError(FranquiciaNotFoundException.class)
                .verify();
    }
}