package ru.mastkey.fj_2024.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class JsonParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParser.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MSG_ERROR_WHILE_PARSING = "Error while parsing json from file with path %s";

    public static <R> Optional<R> parseFromJson(Path path, Class<R> clazz) {
        LOGGER.debug("START PARSING OBJECT TO JSON");
        var jsonFile = path.toFile();

        try {
            return Optional.ofNullable(objectMapper.readValue(jsonFile, clazz));
        } catch (IOException e) {
            LOGGER.error(String.format(MSG_ERROR_WHILE_PARSING, path), e.getMessage(), e);
        } finally {
            LOGGER.debug("END PARSING OBJECT TO JSON");
        }

        return Optional.empty();
    }
}
