package com.nequi.franquicia_app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarSucursalRequest {
    
    private String nuevoNombre;
}