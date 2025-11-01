package com.nequi.franquicia_app.service.impl;

import com.nequi.franquicia_app.dto.request.CrearFranquiciaRequest;
import com.nequi.franquicia_app.model.Franquicia;
import com.nequi.franquicia_app.repository.FranquiciaRepository;
import com.nequi.franquicia_app.service.FranquiciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FranquiciaServiceImpl implements FranquiciaService {
    
    private final FranquiciaRepository franquiciaRepository;
    
    @Override
    public Mono<Franquicia> crearFranquicia(CrearFranquiciaRequest request) {
        Franquicia franquicia = new Franquicia(null, request.getNombre());
        return franquiciaRepository.save(franquicia);
    }
}