package com.nequi.franquicia_app.service;

import com.nequi.franquicia_app.dto.request.ActualizarFranquiciaRequest;
import com.nequi.franquicia_app.dto.request.CrearFranquiciaRequest;
import com.nequi.franquicia_app.model.Franquicia;
import reactor.core.publisher.Mono;

public interface FranquiciaService {
    
    Mono<Franquicia> crearFranquicia(CrearFranquiciaRequest request);
    Mono<Franquicia> actualizarNombre(Long franquiciaId, ActualizarFranquiciaRequest request);
}