package com.nequi.franquicia_app.dto.response;

import com.nequi.franquicia_app.model.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalConProductosResponse {
    private Long id;
    private String nombre;
    private Long franquiciaId;
    private List<Producto> productos;
}