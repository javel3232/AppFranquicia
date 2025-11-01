package com.nequi.franquicia_app.service.impl;

import com.nequi.franquicia_app.dto.request.CrearSucursalRequest;
import com.nequi.franquicia_app.exception.FranquiciaNotFoundException;
import com.nequi.franquicia_app.model.Sucursal;
import com.nequi.franquicia_app.repository.FranquiciaRepository;
import com.nequi.franquicia_app.repository.SucursalRepository;
import com.nequi.franquicia_app.service.SucursalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService {
    
    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;
    
    @Override
    public Mono<Sucursal> crearSucursal(Long franquiciaId, CrearSucursalRequest request) {
        return Mono.just(request)
            .filter(req -> req.getNombre() != null && !req.getNombre().trim().isEmpty())
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Nombre es requerido")))
            .flatMap(req -> franquiciaRepository.existsById(franquiciaId)
                .filter(exists -> exists)
                .switchIfEmpty(Mono.error(new FranquiciaNotFoundException(franquiciaId)))
                .map(exists -> new Sucursal(null, req.getNombre().trim(), franquiciaId)))
            .flatMap(sucursalRepository::save)
            .doOnSuccess(s -> log.info("Sucursal creada: {} para franquicia ID: {}", s.getNombre(), franquiciaId))
            .doOnError(e -> log.error("Error creando sucursal: {}", e.getMessage()));
    }
}