package ru.mastkey.fj_2024.lesson5.suport;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class WiremockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext applicationContext) {
        WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort().withRootDirectory("src/test/resources"));
        wireMockServer.start();

        applicationContext
                .getBeanFactory()
                .registerSingleton("wireMockServer", wireMockServer);

        applicationContext.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                wireMockServer.stop();
            }
        });

        String wireMockUrl = String.format("http://%s:%d", "localhost", wireMockServer.port());
        TestPropertyValues
                .of(new String[]{
                                "kudago.category-url=" + wireMockUrl + "/public-api/v1.4/place-categories",
                                "kudago.location-url=" + wireMockUrl + "/public-api/v1.4/locations",
                                "cb.currency-url=" + wireMockUrl + "/scripts/XML_daily.asp"
                        }
                ).applyTo(applicationContext.getEnvironment());
    }
}
