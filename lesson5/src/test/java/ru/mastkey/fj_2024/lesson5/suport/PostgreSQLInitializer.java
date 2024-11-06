package ru.mastkey.fj_2024.lesson5.suport;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgreSQLInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final DockerImageName IMAGE = DockerImageName.parse("postgres:14-alpine");
    private static final Network NETWORK = Network.newNetwork();
    private static final PostgreSQLContainer<?> CONTAINER;

    public PostgreSQLInitializer() {
    }

    public void initialize(ConfigurableApplicationContext context) {
        ((PostgreSQLContainer) ((PostgreSQLContainer) CONTAINER.withNetwork(NETWORK)).withUrlParam("prepareThreshold", "0")).start();
        TestPropertyValues.of(new String[]{
                "spring.datasource.url=" + CONTAINER.getJdbcUrl(),
                "spring.datasource.username=" + CONTAINER.getUsername(),
                "spring.datasource.password=" + CONTAINER.getPassword(),
                "spring.datasource.driver-class-name=org.postgresql.Driver",
                "spring.liquibase.enabled=true"}
        ).applyTo(context.getEnvironment());
    }

    static {
        CONTAINER = new PostgreSQLContainer(IMAGE);
    }
}