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
            .filter(req -> req.getNuevoNombre() != null && !req.getNuevoNombre().trim().isEmpty())
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Nuevo nombre es requerido")))
            .flatMap(req -> franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new FranquiciaNotFoundException(franquiciaId)))
                .map(franquicia -> {
                    franquicia.setNombre(req.getNuevoNombre().trim());
                    return franquicia;
                }))
            .flatMap(franquiciaRepository::save)
            .doOnSuccess(f -> log.info("Franquicia actualizada: ID {} - Nuevo nombre: {}", franquiciaId, f.getNombre()))
            .doOnError(e -> log.error("Error actualizando franquicia: {}", e.getMessage()));
    }
}