package ru.mastkey.fj_2024.lesson5.suport;

import org.jeasy.random.EasyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.mastkey.fj_2024.lesson5.entity.Category;
import ru.mastkey.fj_2024.lesson5.entity.Location;
import ru.mastkey.fj_2024.lesson5.repository.EntityRepository;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
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

}
