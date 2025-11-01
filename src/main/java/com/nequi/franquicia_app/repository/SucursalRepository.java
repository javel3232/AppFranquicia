package com.nequi.franquicia_app.repository;

import com.nequi.franquicia_app.model.Sucursal;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends ReactiveCrudRepository<Sucursal, Long> {
}