package ru.mastkey.fj_2024.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.mastkey.fj_2024.model.City;
import ru.mastkey.fj_2024.model.Coordinates;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class XmlParserTest {

    @Test
    void parseToXmlSuccessTest(@TempDir Path tempDir) {
        String fileName = tempDir.resolve("success-test.xml").toString();
        var objectToParse = new City("spb", new Coordinates(52d, 52d));

        var xmlFilePath = XmlParser.parseToXml(objectToParse, fileName);

        assertThat(xmlFilePath).isPresent();
    }
}