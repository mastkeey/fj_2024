package ru.mastkey.fj_2024.lesson5.suport;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.mastkey.fj_2024.lesson5.entity.Category;
import ru.mastkey.fj_2024.lesson5.entity.Event;
import ru.mastkey.fj_2024.lesson5.entity.Location;
import ru.mastkey.fj_2024.lesson5.entity.Place;
import ru.mastkey.fj_2024.lesson5.repository.EntityRepository;
import ru.mastkey.fj_2024.lesson5.repository.EventRepository;
import ru.mastkey.fj_2024.lesson5.repository.PlaceRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = {PostgreSQLInitializer.class, WiremockInitializer.class})
public class IntegrationTestBase {

    protected static final EasyRandom easyRandom = new EasyRandom();

    @Autowired
    protected WebTestClient webClient;

    @Autowired
    protected EntityRepository<UUID, Category> categoryRepository;

    @Autowired
    protected EntityRepository<UUID, Location> locationRepository;

    @Autowired
    protected ConversionService conversionService;

    @Autowired
    protected PlaceRepository placeRepository;

    @Autowired
    protected EventRepository eventRepository;

    protected Place createPlaceWithEvent() {
        var place = new Place();
        place.setAddress("testAddress");
        place.setCity("testCity");
        place.setName("testName");
        placeRepository.save(place);

        var event = new Event();
        event.setPlace(place);
        event.setName("test");
        event.setDate(LocalDate.of(2024, 10, 18));

        place.setEvents(List.of(event));

        eventRepository.save(event);
        placeRepository.save(place);
        return place;
    }

    protected Event createEvent() {
        var event = easyRandom.nextObject(Event.class);
        var place = easyRandom.nextObject(Place.class);
        place.setEvents(null);
        var savedPlace = placeRepository.save(place);
        event.setPlace(savedPlace);
        return eventRepository.save(event);
    }

    protected Place createPlace() {
        var place = easyRandom.nextObject(Place.class);
        place.setEvents(null);
        return placeRepository.save(place);
    }

    @AfterEach
    public void clean() {
        placeRepository.deleteAll();
    }

}
