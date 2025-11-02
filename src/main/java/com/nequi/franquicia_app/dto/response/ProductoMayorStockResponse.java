package com.nequi.franquicia_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMayorStockResponse {
    
    private Long productoId;
    private String nombreProducto;
    private Integer stock;
    private Long sucursalId;
    private String nombreSucursal;
}