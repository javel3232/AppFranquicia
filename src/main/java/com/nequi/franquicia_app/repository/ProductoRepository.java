package com.nequi.franquicia_app.repository;

import com.nequi.franquicia_app.model.Producto;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends ReactiveCrudRepository<Producto, Long> {
}