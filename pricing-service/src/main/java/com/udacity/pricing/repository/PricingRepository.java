package com.udacity.pricing.repository;

import org.springframework.data.repository.CrudRepository;

import com.udacity.pricing.domain.price.Price;

public interface PricingRepository extends CrudRepository<Price, Long> {

}
