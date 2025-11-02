package com.nequi.franquicia_app.controller;

import com.nequi.franquicia_app.dto.request.ActualizarSucursalRequest;
import com.nequi.franquicia_app.dto.request.CrearSucursalRequest;
import com.nequi.franquicia_app.exception.FranquiciaNotFoundException;
import com.nequi.franquicia_app.exception.SucursalNotFoundException;
import com.nequi.franquicia_app.model.Sucursal;
import com.nequi.franquicia_app.service.SucursalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
public class SucursalController {
    
    private final SucursalService sucursalService;
    
    @PostMapping("/{franquiciaId}/sucursales")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Sucursal> crearSucursal(
            @PathVariable Long franquiciaId,
            @RequestBody CrearSucursalRequest request) {
        return sucursalService.crearSucursal(franquiciaId, request)
            .onErrorMap(IllegalArgumentException.class, 
                ex -> new RuntimeException("Datos inválidos: " + ex.getMessage()))
            .onErrorMap(FranquiciaNotFoundException.class,
                ex -> new RuntimeException(ex.getMessage()));
    }
    
    @PutMapping("/sucursales/{sucursalId}/nombre")
    public Mono<Sucursal> actualizarNombre(
            @PathVariable Long sucursalId,
            @RequestBody ActualizarSucursalRequest request) {
        return sucursalService.actualizarNombre(sucursalId, request)
            .onErrorMap(IllegalArgumentException.class,
                ex -> new RuntimeException("Datos inválidos: " + ex.getMessage()))
            .onErrorMap(SucursalNotFoundException.class,
                ex -> new RuntimeException(ex.getMessage()));
    }
}