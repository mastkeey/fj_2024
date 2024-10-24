package ru.mastkey.fj_2024.lesson5.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import ru.mastkey.fj_2024.lesson5.client.ApiClient;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoLocationResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.LocationRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.LocationResponse;
import ru.mastkey.fj_2024.lesson5.entity.Location;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;
import ru.mastkey.fj_2024.lesson5.mapper.LocationRequestToLocationMapper;
import ru.mastkey.fj_2024.lesson5.repository.EntityRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LocationServiceUnitTest {

    @Mock
    private EntityRepository<UUID, Location> locationRepository;

    @Mock
    private ApiClient<KudaGoLocationResponse> client;

    @Mock
    private ConversionService conversionService;

    @Mock
    private LocationRequestToLocationMapper locationRequestToLocationMapper;

    @InjectMocks
    private LocationService locationService;

    private UUID locationId;
    private Location location;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        locationId = UUID.randomUUID();
        location = new Location();
        location.setId(locationId);
    }


    @Test
    void getLocationById_ShouldReturnLocation_WhenLocationExists() {
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(conversionService.convert(any(Location.class), eq(LocationResponse.class))).thenReturn(new LocationResponse());

        LocationResponse response = locationService.getLocationById(locationId);

        assertNotNull(response);
        verify(locationRepository, times(1)).findById(locationId);
    }


    @Test
    void getLocationById_ShouldThrowException_WhenLocationNotFound() {
        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            locationService.getLocationById(locationId);
        });

        assertEquals("Location with id '" + locationId + "' not found", exception.getMessage());
    }


    @Test
    void createLocation_ShouldReturnCreatedLocation() {
        LocationRequest request = new LocationRequest();
        when(conversionService.convert(any(LocationRequest.class), eq(Location.class))).thenReturn(location);
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(conversionService.convert(any(Location.class), eq(LocationResponse.class))).thenReturn(new LocationResponse());

        LocationResponse response = locationService.createLocation(request);

        assertNotNull(response);
        verify(locationRepository, times(1)).save(any(Location.class));
    }


    @Test
    void updateLocation_ShouldThrowException_WhenLocationNotFound() {
        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            locationService.updateLocation(locationId, new LocationRequest());
        });

        assertEquals("Location with id '" + locationId + "' not found", exception.getMessage());
    }


    @Test
    void deleteLocationById_ShouldDeleteLocation_WhenLocationExists() {
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));

        locationService.deleteLocationById(locationId);

        verify(locationRepository, times(1)).deleteById(locationId);
    }


    @Test
    void deleteLocationById_ShouldThrowException_WhenLocationNotFound() {
        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            locationService.deleteLocationById(locationId);
        });

        assertEquals("Location with id '" + locationId + "' not found", exception.getMessage());
    }
}
