package ru.mastkey.fj_2024.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;

public class XmlParser {
    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlParser.class);
    private static final String MSG_PARSE_INTO_XML_ERROR = "Failed to parse xml file.";

    public static <T> Optional<Path> parseToXml(T object, String fileName) {
        LOGGER.debug("STAR PARSING OBJECT TO XML");
        var validFileName = fileName.split("\\.")[0] + ".xml";

        try (var fileToSave = new FileOutputStream(validFileName)) {
            var xmlString = xmlMapper.writeValueAsString(object);
            fileToSave.write(xmlString.getBytes(StandardCharsets.UTF_8));

            LOGGER.info("PARSING OBJECT TO XML SUCCESSFULLY");

            return Optional.of(Path.of(validFileName));
        } catch (IOException e) {
            LOGGER.error(MSG_PARSE_INTO_XML_ERROR, e);
        } finally {
            LOGGER.debug("END PARSING OBJECT TO XML");
        }

        return Optional.empty();
    }
}
