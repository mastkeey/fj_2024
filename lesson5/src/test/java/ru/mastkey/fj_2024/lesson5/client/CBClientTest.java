package ru.mastkey.fj_2024.lesson5.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import ru.mastkey.fj_2024.lesson5.client.dto.CBCurrencyResponse;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CBClientTest {
    private  MockedStatic<EntityUtils> entityUtilsMockedStatic;

    @Mock
    private HttpClient httpClient;

    @Mock
    private XmlMapper xmlMapper;

    @Mock
    private CloseableHttpResponse httpResponse;

    @Mock
    private HttpEntity entity;

    @InjectMocks
    private CBClient cbClient;

    @BeforeEach
    void setUp() {
        entityUtilsMockedStatic = mockStatic(EntityUtils.class);
        MockitoAnnotations.openMocks(this);
        cbClient.BASE_URL = "http://example.com/";
    }

    @Test
    void getAllCurrenciesFromCB_ShouldReturnCurrencyList_WhenRequestIsSuccessful() throws Exception {
        String xmlResponse = "<Currencies>" +
                "<Currency ID='R01010'>" +
                "<NumCode>036</NumCode>" +
                "<CharCode>AUD</CharCode>" +
                "<Nominal>1</Nominal>" +
                "<Name>Australian Dollar</Name>" +
                "<Value>55.1234</Value>" +
                "</Currency>" +
                "<Currency ID='R01020'>" +
                "<NumCode>124</NumCode>" +
                "<CharCode>CAD</CharCode>" +
                "<Nominal>1</Nominal>" +
                "<Name>Canadian Dollar</Name>" +
                "<Value>60.5678</Value>" +
                "</Currency>" +
                "</Currencies>";

        List<CBCurrencyResponse> mockResponse = List.of(
                new CBCurrencyResponse("R01010", 36, "AUD", 1, "Australian Dollar", "55.1234", "55.1234"),
                new CBCurrencyResponse("R01020", 124, "CAD", 1, "Canadian Dollar", "60.5678", "60.5678")
        );

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(entity);
        entityUtilsMockedStatic.when(() -> EntityUtils.toString(entity, "UTF-8")).thenReturn(xmlResponse);
        when(xmlMapper.readValue(eq(xmlResponse), any(TypeReference.class))).thenReturn(mockResponse);

        List<CBCurrencyResponse> result = cbClient.getCurrencies();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("R01010", result.get(0).getId());
        assertEquals("Australian Dollar", result.get(0).getName());
        assertEquals("55.1234", result.get(0).getValue());
        assertEquals("R01020", result.get(1).getId());
        assertEquals("Canadian Dollar", result.get(1).getName());
        assertEquals("60.5678", result.get(1).getValue());
    }

    @Test
    void getAllCurrenciesFromCB_ShouldThrowServiceException_WhenIOExceptionOccurs() throws Exception {
        when(httpClient.execute(any(HttpGet.class))).thenThrow(new IOException("IO error"));

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            cbClient.getCurrencies();
        });

        assertEquals(ErrorType.SERVICE_UNAVAILABLE.getCode(), exception.getCode());
        assertEquals(exception.getHeaders().get("Retry-After"), "3600");
    }

    @AfterEach
    void tearDown() {
        entityUtilsMockedStatic.close();
    }
}