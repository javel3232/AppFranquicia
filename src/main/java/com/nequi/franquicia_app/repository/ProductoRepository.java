package com.nequi.franquicia_app.repository;

import com.nequi.franquicia_app.model.Producto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductoRepository extends ReactiveCrudRepository<Producto, Long> {
    
    @Query("SELECT p.* FROM productos p " +
           "INNER JOIN sucursales s ON p.sucursal_id = s.id " +
           "WHERE s.franquicia_id = :franquiciaId")
    Flux<Producto> findByFranquiciaId(Long franquiciaId);
}