package com.nequi.franquicia_app.controller;

import com.nequi.franquicia_app.dto.request.ActualizarFranquiciaRequest;
import com.nequi.franquicia_app.dto.request.CrearFranquiciaRequest;
import com.nequi.franquicia_app.exception.FranquiciaNotFoundException;
import com.nequi.franquicia_app.model.Franquicia;
import com.nequi.franquicia_app.service.FranquiciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
public class FranquiciaController {
    
    private static final String INVALID_DATA = "Invalid data: ";
    
    private final FranquiciaService franquiciaService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franquicia> crearFranquicia(@RequestBody CrearFranquiciaRequest request) {
        return franquiciaService.crearFranquicia(request)
            .onErrorMap(IllegalArgumentException.class, 
                ex -> new RuntimeException(INVALID_DATA + ex.getMessage()));
    }
    
    @PutMapping("/{franquiciaId}/nombre")
    public Mono<Franquicia> actualizarNombre(
            @PathVariable Long franquiciaId,
            @RequestBody ActualizarFranquiciaRequest request) {
        return franquiciaService.actualizarNombre(franquiciaId, request)
            .onErrorMap(IllegalArgumentException.class,
                ex -> new RuntimeException(INVALID_DATA + ex.getMessage()))
            .onErrorMap(FranquiciaNotFoundException.class,
                ex -> new RuntimeException(ex.getMessage()));
    }
    
    @GetMapping
    public Flux<Franquicia> obtenerTodasLasFranquicias() {
        return franquiciaService.obtenerTodasLasFranquicias();
    }
    
    @GetMapping("/{franquiciaId}")
    public Mono<Franquicia> obtenerFranquiciaPorId(@PathVariable Long franquiciaId) {
        return franquiciaService.obtenerFranquiciaPorId(franquiciaId)
            .onErrorMap(IllegalArgumentException.class,
                ex -> new RuntimeException(INVALID_DATA + ex.getMessage()))
            .onErrorMap(FranquiciaNotFoundException.class,
                ex -> new RuntimeException(ex.getMessage()));
    }
}