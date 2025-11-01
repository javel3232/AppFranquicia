package com.nequi.franquicia_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("sucursales")
public class Sucursal {

    @Id
    private Long id;

    private String nombre;
    private Long franquiciaId;
}