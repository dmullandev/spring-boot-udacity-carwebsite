package com.udacity.vehicles.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;

/**
 * Implements the car service create, read, update or delete information about
 * vehicles, as well as gather related location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;
    private final PriceClient pricingClient;
    private final MapsClient mapsClient;

    public CarService(CarRepository repository, PriceClient pricingClient, MapsClient mapsClient) {
        this.repository = repository;
        this.pricingClient = pricingClient;
        this.mapsClient = mapsClient;
    }

    /**
     * Gathers a list of all vehicles
     * 
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * 
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        if (id == null)
            throw new IllegalArgumentException();

        Optional<Car> carOptional = repository.findById(id);

        if (carOptional.isEmpty())
            throw new CarNotFoundException();

        carOptional.get().setPrice(pricingClient.getPrice(id));
        carOptional.get().setLocation(mapsClient.getAddress(carOptional.get().getLocation()));

        return carOptional.get();
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * 
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId()).map(carToBeUpdated -> {
                carToBeUpdated.setDetails(car.getDetails());
                carToBeUpdated.setLocation(car.getLocation());
                return repository.save(carToBeUpdated);
            }).orElseThrow(CarNotFoundException::new);
        }

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     * 
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {

        if (id == null)
            throw new IllegalArgumentException();

        Optional<Car> carOptional = repository.findById(id);

        if (carOptional.isPresent()) {
            repository.delete(carOptional.get());
        } else {
            throw new CarNotFoundException();
        }
    }
}
