package com.udacity.pricing.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.udacity.pricing.entity.Price;

@RepositoryRestResource(collectionResourceRel = "prices", path = "prices")
public interface PriceRepository extends CrudRepository<Price, Long> {
    Optional<Price> findByVehicleId(@Param("vehicleId") Long vehicleId);
}
