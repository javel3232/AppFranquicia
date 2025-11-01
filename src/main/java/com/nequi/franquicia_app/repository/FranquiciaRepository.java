package com.nequi.franquicia_app.repository;

import com.nequi.franquicia_app.model.Franquicia;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranquiciaRepository extends ReactiveCrudRepository<Franquicia, Long> {
}