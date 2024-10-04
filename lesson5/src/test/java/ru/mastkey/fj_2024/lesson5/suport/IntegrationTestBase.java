package ru.mastkey.fj_2024.lesson5.suport;

import org.jeasy.random.EasyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import ru.mastkey.fj_2024.lesson5.entity.Category;
import ru.mastkey.fj_2024.lesson5.entity.Location;
import ru.mastkey.fj_2024.lesson5.repository.EntityRepository;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class IntegrationTestBase {

    protected static final EasyRandom easyRandom = new EasyRandom();

    @LocalServerPort
    private int port;

    @Container
    private static final WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:2.35.0")
            .withMappingFromResource("wiremock/categories.json")
            .withMappingFromResource("wiremock/locations.json");


    private String wireMockUrl;

    @DynamicPropertySource
    static void overrideKudaGoProperties(DynamicPropertyRegistry registry) {
        String wireMockUrl = String.format("http://%s:%d",
                wiremockServer.getHost(),
                wiremockServer.getMappedPort(8080));
        registry.add("kudago.category-url", () -> wireMockUrl + "/public-api/v1.4/place-categories");
        registry.add("kudago.location-url", () -> wireMockUrl + "/public-api/v1.4/locations");
    }

    @Autowired
    protected WebTestClient webClient;

    @Autowired
    protected EntityRepository<UUID, Category> categoryRepository;

    @Autowired
    protected EntityRepository<UUID, Location> locationRepository;

    @Autowired
    protected ConversionService conversionService;

}
