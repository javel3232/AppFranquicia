package com.nequi.franquicia_app.service;

import com.nequi.franquicia_app.dto.request.ActualizarSucursalRequest;
import com.nequi.franquicia_app.dto.request.CrearSucursalRequest;
import com.nequi.franquicia_app.model.Sucursal;
import reactor.core.publisher.Mono;

public interface SucursalService {
    
    Mono<Sucursal> crearSucursal(Long franquiciaId, CrearSucursalRequest request);
    Mono<Sucursal> actualizarNombre(Long sucursalId, ActualizarSucursalRequest request);
}