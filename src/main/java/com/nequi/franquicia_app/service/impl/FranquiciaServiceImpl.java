package com.nequi.franquicia_app.service.impl;

import com.nequi.franquicia_app.dto.request.ActualizarFranquiciaRequest;
import com.nequi.franquicia_app.dto.request.CrearFranquiciaRequest;
import com.nequi.franquicia_app.exception.FranquiciaNotFoundException;
import com.nequi.franquicia_app.model.Franquicia;
import com.nequi.franquicia_app.repository.FranquiciaRepository;
import com.nequi.franquicia_app.service.FranquiciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FranquiciaServiceImpl implements FranquiciaService {
    
    private final FranquiciaRepository franquiciaRepository;
    
    @Override
    public Mono<Franquicia> crearFranquicia(CrearFranquiciaRequest request) {
        return Mono.just(request)
            .filter(req -> req.getNombre() != null && !req.getNombre().trim().isEmpty())
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Nombre es requerido")))
            .map(req -> new Franquicia(null, req.getNombre().trim()))
            .flatMap(franquiciaRepository::save)
            .doOnSuccess(f -> log.info("Franquicia creada: {}", f.getNombre()))
            .doOnError(e -> log.error("Error creando franquicia: {}", e.getMessage()));
    }
    
    @Override
    public Mono<Franquicia> actualizarNombre(Long franquiciaId, ActualizarFranquiciaRequest request) {
        if (franquiciaId == null) {
            return Mono.error(new IllegalArgumentException("ID de franquicia es requerido"));
        }
        return Mono.just(request)
            .filter(req -> req.getNombre() != null && !req.getNombre().trim().isEmpty())
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Nombre es requerido")))
            .flatMap(req -> franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new FranquiciaNotFoundException(franquiciaId)))
                .map(franquicia -> {
                    franquicia.setNombre(req.getNombre().trim());
                    return franquicia;
                }))
            .flatMap(franquiciaRepository::save)
            .doOnSuccess(f -> log.info("Franquicia actualizada: ID {} - Nuevo nombre: {}", franquiciaId, f.getNombre()))
            .doOnError(e -> log.error("Error actualizando franquicia: {}", e.getMessage()));
    }
    
    @Override
    public Flux<Franquicia> obtenerTodasLasFranquicias() {
        return franquiciaRepository.findAll()
            .doOnNext(f -> log.debug("Franquicia encontrada: {}", f.getNombre()))
            .doOnError(e -> log.error("Error obteniendo franquicias: {}", e.getMessage()));
    }
    
    @Override
    public Mono<Franquicia> obtenerFranquiciaPorId(Long franquiciaId) {
        if (franquiciaId == null) {
            return Mono.error(new IllegalArgumentException("ID de franquicia es requerido"));
        }
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new FranquiciaNotFoundException(franquiciaId)))
            .doOnSuccess(f -> log.info("Franquicia encontrada: ID {} - {}", franquiciaId, f.getNombre()))
            .doOnError(e -> log.error("Error obteniendo franquicia por ID: {}", e.getMessage()));
    }
}