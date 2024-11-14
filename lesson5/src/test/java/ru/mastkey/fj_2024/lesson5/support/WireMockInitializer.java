package ru.mastkey.fj_2024.lesson5.support;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.Network;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import org.testcontainers.utility.DockerImageName;

public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final DockerImageName IMAGE = DockerImageName.parse("wiremock/wiremock:2.35.0");
    private static final Network NETWORK = Network.newNetwork();
    private static final WireMockContainer WIREMOCK_CONTAINER;

    static {
        WIREMOCK_CONTAINER = new WireMockContainer(IMAGE)
                .withNetwork(NETWORK)
                .withExposedPorts(8080)
                .withMappingFromResource("wiremock/categories.json")
                .withMappingFromResource("wiremock/locations.json")
                .withFileSystemBind("src/test/resources/__files/currency.xml", "/home/wiremock/__files/currency.xml")
                .withMappingFromResource("wiremock/currency.json");
        WIREMOCK_CONTAINER.start();
    }

    public void initialize(ConfigurableApplicationContext context) {
        String wireMockUrl = String.format("http://%s:%d",
                WIREMOCK_CONTAINER.getHost(),
                WIREMOCK_CONTAINER.getMappedPort(8080));

        TestPropertyValues.of(
                "kudago.category-url=" + wireMockUrl + "/public-api/v1.4/place-categories",
                "kudago.location-url=" + wireMockUrl + "/public-api/v1.4/locations",
                "cb.currency-url=" + wireMockUrl + "/scripts/XML_daily.asp"
        ).applyTo(context.getEnvironment());
    }
}