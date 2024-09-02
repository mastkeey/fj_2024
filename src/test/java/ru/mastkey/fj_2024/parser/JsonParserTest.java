package ru.mastkey.fj_2024.parser;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.model.City;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class JsonParserTest {

    @Test
    void testParseFromJsonCityJsonSuccess() {
        var validResource = Path.of(ClassLoader.getSystemResource("test-city.json").getPath());

        var parsedObject = JsonParser.parseFromJson(validResource, City.class);

        assertThat(parsedObject).isPresent();
    }

    @Test
    void testParseFromJsonCityJsonFailure() {
        var notValidResource = Path.of(ClassLoader.getSystemResource("test-city-error.json").getPath());

        var parsedObject = JsonParser.parseFromJson(notValidResource, City.class);

        assertThat(parsedObject).isEmpty();
    }
}