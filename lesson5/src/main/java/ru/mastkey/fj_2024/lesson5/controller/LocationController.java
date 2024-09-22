package ru.mastkey.fj_2024.lesson5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mastkey.fj_2024.lesson5.controller.dto.LocationRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.LocationResponse;
import ru.mastkey.fj_2024.lesson5.service.LocationService;
import ru.mastkey.ripperstarter.annotation.ExecutionTime;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/places/locations")
@RequiredArgsConstructor
@ExecutionTime
public class LocationController implements BaseController<LocationRequest, LocationResponse> {

    private final LocationService locationService;

    @Override
    public ResponseEntity<LocationResponse> getById(UUID id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @Override
    public ResponseEntity<List<LocationResponse>> getAll() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @Override
    public ResponseEntity<LocationResponse> create(LocationRequest request) {
        return ResponseEntity.ok(locationService.createLocation(request));
    }

    @Override
    public ResponseEntity<LocationResponse> update(UUID id, LocationRequest request) {
        return ResponseEntity.ok(locationService.updateLocation(id, request));
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        locationService.deleteLocationById(id);
        return ResponseEntity.ok().build();
    }
}
