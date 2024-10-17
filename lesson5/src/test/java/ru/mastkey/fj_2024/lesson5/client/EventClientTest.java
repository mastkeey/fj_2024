package ru.mastkey.fj_2024.lesson5.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
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
import reactor.core.publisher.Flux;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoEventsResponse;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventClientTest {

    private MockedStatic<EntityUtils> entityUtilsMockedStatic;

    @Mock
    private HttpClient httpClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CloseableHttpResponse httpResponse;

    @Mock
    private HttpEntity entity;

    @Mock
    private Semaphore rateLimiter;

    @InjectMocks
    private EventClient eventClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entityUtilsMockedStatic = mockStatic(EntityUtils.class);
        eventClient.BASE_URL = "http://example.com/api/events";
        eventClient.rateLimiter = rateLimiter;
    }

    @AfterEach
    void tearDown() {
        entityUtilsMockedStatic.close();
    }

    @Test
    void getEvents_ShouldReturnEventList_WhenRequestIsSuccessful() throws Exception {
        String jsonResponse = "{ \"results\": ["
                + "{ \"is_free\": true, \"title\": \"Event 1\", \"price\": \"500\" },"
                + "{ \"is_free\": false, \"title\": \"Event 2\", \"price\": \"1500\" }"
                + "] }";

        List<KudaGoEventsResponse> expectedResponse = List.of(
                new KudaGoEventsResponse(true, "Event 1", "500"),
                new KudaGoEventsResponse(false, "Event 2", "1500")
        );

        doNothing().when(rateLimiter).acquire();

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(entity);

        entityUtilsMockedStatic.when(() -> EntityUtils.toString(entity, "UTF-8")).thenReturn(jsonResponse);

        JsonNode rootNode = mock(JsonNode.class);
        JsonNode resultsNode = mock(JsonNode.class);
        when(objectMapper.readTree(jsonResponse)).thenReturn(rootNode);
        when(rootNode.get("results")).thenReturn(resultsNode);

        ObjectReader objectReader = mock(ObjectReader.class);
        when(objectMapper.readerForListOf(KudaGoEventsResponse.class)).thenReturn(objectReader);
        when(objectReader.readValue(resultsNode)).thenReturn(expectedResponse);

        List<KudaGoEventsResponse> result = eventClient.getEvents(LocalDate.now(), LocalDate.now());

        assertNotNull(result);
        assertEquals(expectedResponse.size(), result.size());
        assertEquals(expectedResponse.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(expectedResponse.get(0).getPrice(), result.get(0).getPrice());
        assertEquals(expectedResponse.get(0).getIsFree(), result.get(0).getIsFree());

        assertEquals(expectedResponse.get(1).getTitle(), result.get(1).getTitle());
        assertEquals(expectedResponse.get(1).getPrice(), result.get(1).getPrice());
        assertEquals(expectedResponse.get(1).getIsFree(), result.get(1).getIsFree());

        verify(rateLimiter).acquire();
        verify(rateLimiter).release();
    }

    @Test
    void getEvents_ShouldThrowServiceException_WhenIOExceptionOccurs() throws Exception {
        doNothing().when(rateLimiter).acquire();

        when(httpClient.execute(any(HttpGet.class))).thenThrow(new IOException("Test IO Exception"));

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            eventClient.getEvents(LocalDate.now(), LocalDate.now());
        });

        assertEquals(ErrorType.INTERNAL_SERVER_ERROR.getCode(), exception.getCode());

        verify(rateLimiter).acquire();
        verify(rateLimiter).release();
    }

    @Test
    void getEventsFlux_ShouldReturnFlux_WhenRequestIsSuccessful() throws Exception {
        String jsonResponse = "{\"results\": [{\"is_free\": true, \"title\": \"Test Event 1\", \"price\": \"500\"}, " +
                "{\"is_free\": true, \"title\": \"Test Event 2\", \"price\": \"1000\"}]}";

        List<KudaGoEventsResponse> expectedResponse = List.of(
                new KudaGoEventsResponse(true, "Event 1", "500"),
                new KudaGoEventsResponse(false, "Event 2", "1500")
        );

        doNothing().when(rateLimiter).acquire();

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(entity);

        entityUtilsMockedStatic.when(() -> EntityUtils.toString(entity, "UTF-8")).thenReturn(jsonResponse);

        JsonNode rootNode = mock(JsonNode.class);
        JsonNode resultsNode = mock(JsonNode.class);
        when(objectMapper.readTree(jsonResponse)).thenReturn(rootNode);
        when(rootNode.get("results")).thenReturn(resultsNode);

        ObjectReader objectReader = mock(ObjectReader.class);
        when(objectMapper.readerForListOf(KudaGoEventsResponse.class)).thenReturn(objectReader);
        when(objectReader.readValue(resultsNode)).thenReturn(expectedResponse);

        Flux<KudaGoEventsResponse> resultFlux = eventClient.getEventsFlux(LocalDate.now(), LocalDate.now());
        List<KudaGoEventsResponse> result = resultFlux.collectList().block();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Event 1", result.get(0).getTitle());
        assertEquals("500", result.get(0).getPrice());
        assertEquals("Event 2", result.get(1).getTitle());
        assertEquals("1500", result.get(1).getPrice());

        verify(rateLimiter).acquire();
        verify(rateLimiter).release();
    }

    @Test
    void getEvents_ShouldBlockOnRateLimitExhaustion() throws Exception {
        String jsonResponse = "{ \"results\": [{ \"is_free\": true, \"title\": \"Event 1\", \"price\": \"500\" }] }";

        List<KudaGoEventsResponse> expectedResponse = List.of(
                new KudaGoEventsResponse(true, "Event 1", "500")
        );

        doNothing().when(rateLimiter).acquire();
        doNothing().when(rateLimiter).release();

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(entity);
        entityUtilsMockedStatic.when(() -> EntityUtils.toString(entity, "UTF-8")).thenReturn(jsonResponse);

        JsonNode rootNode = mock(JsonNode.class);
        JsonNode resultsNode = mock(JsonNode.class);
        when(objectMapper.readTree(jsonResponse)).thenReturn(rootNode);
        when(rootNode.get("results")).thenReturn(resultsNode);

        ObjectReader objectReader = mock(ObjectReader.class);
        when(objectMapper.readerForListOf(KudaGoEventsResponse.class)).thenReturn(objectReader);
        when(objectReader.readValue(resultsNode)).thenReturn(expectedResponse);

        doAnswer(invocation -> {
            throw new InterruptedException("Rate limiter exhausted");
        }).when(rateLimiter).acquire();

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            eventClient.getEvents(LocalDate.now(), LocalDate.now());
        });

        assertEquals(ErrorType.INTERNAL_SERVER_ERROR.getCode(), exception.getCode());

        verify(rateLimiter).acquire();
    }
}