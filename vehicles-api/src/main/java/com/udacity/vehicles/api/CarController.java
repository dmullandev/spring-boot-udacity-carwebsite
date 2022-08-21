package com.udacity.vehicles.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.service.CarNotFoundException;
import com.udacity.vehicles.service.CarService;

/**
 * Implements a REST-based controller for the Vehicles API.
 */
@RestController
@RequestMapping("/cars")
class CarController {

    private final CarService carService;
    private final CarResourceAssembler assembler;

    CarController(CarService carService, CarResourceAssembler assembler) {
        this.carService = carService;
        this.assembler = assembler;
    }

    /**
     * Creates a list to store any vehicles.
     * 
     * @return list of vehicles
     */
    @GetMapping
    Resources<Resource<Car>> list() {
        List<Resource<Car>> resources = carService.list().stream().map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(resources, linkTo(methodOn(CarController.class).list()).withSelfRel());
    }

    /**
     * Gets information of a specific car by ID.
     * 
     * @param id the id number of the given vehicle
     * @return all information for the requested vehicle
     */
    @GetMapping("/{id}")
    Resource<Car> get(@PathVariable Long id) {
        try {
            return assembler.toResource(carService.findById(id));
        } catch (CarNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    MessageFormat.format("Vehicle ID ''{0}'' not found", id), e);
        }
    }

    /**
     * Posts information to create a new vehicle in the system.
     * 
     * @param car A new vehicle to add to the system.
     * @return response that the new vehicle was added to the system
     * @throws URISyntaxException if the request contains invalid fields or syntax
     */
    @PostMapping
    ResponseEntity<?> post(@Valid @RequestBody Car car) throws URISyntaxException {
        try {
            Resource<Car> resource = assembler.toResource(carService.save(car));
            return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
        } catch (Exception e) {
            throw new URISyntaxException(e.toString(),
                    MessageFormat.format("Error creating vehicle with id: ''{0}'' ", car.getId()));
        }
    }

    /**
     * Updates the information of a vehicle in the system.
     * 
     * @param id  The ID number for which to update vehicle information.
     * @param car The updated information about the related vehicle.
     * @return response that the vehicle was updated in the system
     */
    @PutMapping("/{id}")
    ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody Car car) {
        try {
            car.setId(id);
            Resource<Car> resource = assembler.toResource(carService.save(car));
            return ResponseEntity.ok(resource);
        } catch (CarNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    MessageFormat.format("Vehicle ID ''{0}'' not found", id), e);
        }
    }

    /**
     * Removes a vehicle from the system.
     * 
     * @param id The ID number of the vehicle to remove.
     * @return response that the related vehicle is no longer in the system
     */
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            carService.delete(id);
            return ResponseEntity.ok("Vehicle deleted successfully");
        } catch (CarNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    MessageFormat.format("Vehicle ID ''{0}'' not found", id), e);
        }
    }
}
