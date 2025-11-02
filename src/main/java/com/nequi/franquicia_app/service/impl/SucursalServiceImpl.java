package com.nequi.franquicia_app.service.impl;

import com.nequi.franquicia_app.dto.request.ActualizarSucursalRequest;
import com.nequi.franquicia_app.dto.request.CrearSucursalRequest;
import com.nequi.franquicia_app.exception.FranquiciaNotFoundException;
import com.nequi.franquicia_app.exception.SucursalNotFoundException;
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
    public Mono<Sucursal> crearSucursal(Long franchiseId, CrearSucursalRequest request) {
        if (franchiseId == null) {
            return Mono.error(new IllegalArgumentException("Franchise ID is required"));
        }
        return Mono.just(request)
            .filter(req -> req.getNombre() != null && !req.getNombre().trim().isEmpty())
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Nombre es requerido")))
            .flatMap(req -> franquiciaRepository.existsById(franchiseId)
                .filter(exists -> exists)
                .switchIfEmpty(Mono.error(new FranquiciaNotFoundException(franchiseId)))
                .map(exists -> new Sucursal(null, req.getNombre().trim(), franchiseId)))
            .flatMap(sucursalRepository::save)
            .doOnSuccess(s -> log.info("Sucursal creada: {} para franchise ID: {}", s.getNombre(), franchiseId))
            .doOnError(e -> log.error("Error creando sucursal: {}", e.getMessage()));
    }
    
    @Override
    public Mono<Sucursal> actualizarNombre(Long sucursalId, ActualizarSucursalRequest request) {
        if (sucursalId == null) {
            return Mono.error(new IllegalArgumentException("ID de sucursal es requerido"));
        }
        return Mono.just(request)
            .filter(req -> req.getNombre() != null && !req.getNombre().trim().isEmpty())
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Nombre es requerido")))
            .flatMap(req -> sucursalRepository.findById(sucursalId)
                .switchIfEmpty(Mono.error(new SucursalNotFoundException(sucursalId)))
                .map(sucursal -> {
                    sucursal.setNombre(req.getNombre().trim());
                    return sucursal;
                }))
            .flatMap(sucursalRepository::save)
            .doOnSuccess(s -> log.info("Sucursal actualizada: ID {} - Nuevo nombre: {}", sucursalId, s.getNombre()))
            .doOnError(e -> log.error("Error actualizando sucursal: {}", e.getMessage()));
    }
}