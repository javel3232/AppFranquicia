package com.nequi.franquicia_app.service;

import com.nequi.franquicia_app.dto.request.CrearSucursalRequest;
import com.nequi.franquicia_app.exception.FranquiciaNotFoundException;
import com.nequi.franquicia_app.exception.SucursalNotFoundException;
import com.nequi.franquicia_app.model.Sucursal;
import com.nequi.franquicia_app.repository.FranquiciaRepository;
import com.nequi.franquicia_app.repository.SucursalRepository;
import com.nequi.franquicia_app.service.impl.SucursalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private SucursalServiceImpl sucursalService;

    @Test
    void createBranch_ShouldCreateBranch_WhenValidData() {
        Long franchiseId = 1L;
        CrearSucursalRequest request = new CrearSucursalRequest("Centro");
        var branch = new Sucursal(1L, "Centro", franchiseId);
        
        when(franquiciaRepository.existsById(franchiseId)).thenReturn(Mono.just(true));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(Mono.just(branch));
        StepVerifier.create(sucursalService.crearSucursal(franchiseId, request))
                .expectNextMatches(result -> 
                    result.getId().equals(1L) && "Centro".equals(result.getNombre()))
                .verifyComplete();
    }

    @Test
    void createBranch_ShouldFail_WhenFranchiseNotExists() {
        Long franchiseId = 999L;
        CrearSucursalRequest request = new CrearSucursalRequest("Centro");
        
        when(franquiciaRepository.existsById(franchiseId)).thenReturn(Mono.just(false));
        StepVerifier.create(sucursalService.crearSucursal(franchiseId, request))
                .expectError(FranquiciaNotFoundException.class)
                .verify();
    }

    @Test
    void updateName_ShouldFail_WhenBranchNotExists() {
        Long branchId = 999L;
        var request = new com.nequi.franquicia_app.dto.request.ActualizarSucursalRequest("Nuevo Nombre");
        
        when(sucursalRepository.findById(branchId)).thenReturn(Mono.empty());
        StepVerifier.create(sucursalService.actualizarNombre(branchId, request))
                .expectError(SucursalNotFoundException.class)
                .verify();
    }
}