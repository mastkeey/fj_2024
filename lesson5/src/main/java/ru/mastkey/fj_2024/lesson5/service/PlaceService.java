package ru.mastkey.fj_2024.lesson5.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mastkey.fj_2024.lesson5.controller.dto.PlaceRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.PlaceResponse;
import ru.mastkey.fj_2024.lesson5.entity.Place;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;
import ru.mastkey.fj_2024.lesson5.mapper.EventRequestToEventMapper;
import ru.mastkey.fj_2024.lesson5.mapper.PlaceRequestToPlaceMapper;
import ru.mastkey.fj_2024.lesson5.repository.PlaceRepository;

import java.util.List;
import java.util.UUID;

import static ru.mastkey.fj_2024.lesson5.util.Constants.MSG_PLACE_EXIST;
import static ru.mastkey.fj_2024.lesson5.util.Constants.MSG_PLACE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final ConversionService conversionService;
    private final PlaceRequestToPlaceMapper placeRequestToPlaceMapper;

    @Transactional(readOnly = true)
    public PlaceResponse getPlaceById(UUID id) {
        var place = validateAndGetPlace(id);
        return conversionService.convert(place, PlaceResponse.class);
    }

    @Transactional(readOnly = true)
    public List<PlaceResponse> getAllPlaces() {
        var places = placeRepository.findAll();
        return places.stream()
                .map(place -> conversionService.convert(place, PlaceResponse.class))
                .toList();
    }

    @Transactional
    public PlaceResponse createPlace(PlaceRequest placeRequest) {
        if (placeRepository.findByNameAndAddressAndCity(placeRequest.getName(), placeRequest.getAddress(), placeRequest.getCity()).isPresent()) {
            throw new ServiceException(ErrorType.CONFLICT, MSG_PLACE_EXIST, placeRequest.getName(), placeRequest.getAddress(), placeRequest.getCity());
        }

        var place = conversionService.convert(placeRequest, Place.class);

        return conversionService.convert(placeRepository.save(place), PlaceResponse.class);
    }

    @Transactional
    public PlaceResponse updatePlace(PlaceRequest placeRequest, UUID id) {
        var place = validateAndGetPlace(id);

        var placeToSave = placeRequestToPlaceMapper.toPlaceForUpdate(place, placeRequest);

        return conversionService.convert(placeRepository.save(placeToSave), PlaceResponse.class);
    }

    @Transactional
    public void deletePlaceById(UUID id) {
        var place = validateAndGetPlace(id);
        placeRepository.delete(place);
    }

    private Place validateAndGetPlace(UUID id) {
        var place = placeRepository.findById(id);
        if (place.isEmpty()) {
            throw new ServiceException(ErrorType.NOT_FOUND, MSG_PLACE_NOT_FOUND, id);
        }
        return place.get();
    }
}
