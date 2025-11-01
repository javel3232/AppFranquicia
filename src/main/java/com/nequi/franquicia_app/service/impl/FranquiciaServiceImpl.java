package com.nequi.franquicia_app.service.impl;

import com.nequi.franquicia_app.dto.request.CrearFranquiciaRequest;
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
}