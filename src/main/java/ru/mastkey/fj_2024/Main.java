package ru.mastkey.fj_2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mastkey.fj_2024.model.City;
import ru.mastkey.fj_2024.parser.JsonParser;
import ru.mastkey.fj_2024.parser.XmlParser;

import java.nio.file.Path;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String MSG_WARNING_WITH_JSON = "Exception while parsing json";
    private static final String MSG_PARSED_OBJECT = "Parsed object: %s";
    private static final String MSG_PARSED_XML = "Parsed object into xml path: %s";
    private static final String MSG_WARNING_WITH_XML = "Exception while parsing xml";

    public static void main(String[] args) {
        var notValidResource = Path.of(ClassLoader.getSystemResource("city-error.json").getPath());
        var parsedNotValidObject = JsonParser.parseFromJson(notValidResource, City.class);

        if (parsedNotValidObject.isPresent()) {
            LOGGER.info(String.format(MSG_PARSED_OBJECT, parsedNotValidObject.get()));
        } else {
            LOGGER.warn(MSG_WARNING_WITH_JSON);
        }

        var validResource = Path.of(ClassLoader.getSystemResource("city.json").getPath());
        var parsedValidObject = JsonParser.parseFromJson(validResource, City.class);

        if (parsedValidObject.isPresent()) {
            LOGGER.info(String.format(MSG_PARSED_OBJECT, parsedValidObject.get()));
        } else {
            LOGGER.warn(MSG_WARNING_WITH_JSON);
        }

        var parsedIntoXmlCityPath = XmlParser.parseToXml(parsedValidObject.get(), "city");

        if (parsedIntoXmlCityPath.isPresent()) {
            LOGGER.info(String.format(MSG_PARSED_XML, parsedIntoXmlCityPath.get()));
        } else {
            LOGGER.warn(MSG_WARNING_WITH_XML);
        }
    }
}
