package ru.mastkey.fj_2024.lesson5.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.mastkey.fj_2024.lesson5.client.ApiClient;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoLocationResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.LocationRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.LocationResponse;
import ru.mastkey.fj_2024.lesson5.entity.Location;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;
import ru.mastkey.fj_2024.lesson5.mapper.LocationRequestToLocationMapper;
import ru.mastkey.fj_2024.lesson5.repository.EntityRepository;
import ru.mastkey.ripperstarter.annotation.ExecutionTime;
import ru.mastkey.ripperstarter.annotation.PostProxyPostConstruct;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {
    private static final String MSG_LOCATION_NOT_FOUND = "Location with id '%s' not found";
    private final EntityRepository<UUID, Location> locationRepository;
    private final ApiClient<KudaGoLocationResponse> client;
    private final ConversionService conversionService;
    private final LocationRequestToLocationMapper locationRequestToLocationMapper;

    @ExecutionTime
    @PostProxyPostConstruct
    public void init() {
        log.info("location init start");
        var kudaGoLocationResponses = client.getAllEntitiesFromKudaGo();
        var locations = kudaGoLocationResponses.stream()
                .map(res -> conversionService.convert(res, Location.class))
                .toList();
        locationRepository.saveAll(locations);
        log.info("location init end");
    }

    public LocationResponse getLocationById(UUID uuid) {
        var location = findLocationWithValidation(uuid);
        return conversionService.convert(location, LocationResponse.class);
    }

    public List<LocationResponse> getAllLocations() {
        var locations = locationRepository.findAll();

        return locations.stream()
                .map(location -> conversionService.convert(location, LocationResponse.class))
                .toList();
    }

    public LocationResponse createLocation(LocationRequest request) {
        var location = conversionService.convert(request, Location.class);

        return conversionService.convert(locationRepository.save(location), LocationResponse.class);
    }

    public void deleteLocationById(UUID id) {
        locationRepository.deleteById(id);
    }

    public LocationResponse updateLocation(UUID id, LocationRequest request) {
        var location = findLocationWithValidation(id);
        var updatedLocation = locationRequestToLocationMapper.toLocationForUpdate(location, request);

        return conversionService.convert(locationRepository.save(updatedLocation), LocationResponse.class);
    }

    private Location findLocationWithValidation(UUID id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorType.NOT_FOUND, MSG_LOCATION_NOT_FOUND, id));
    }
}
