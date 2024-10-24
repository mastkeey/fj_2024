package ru.mastkey.fj_2024.lesson5.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoLocationResponse;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LocationClientTest {

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
    private LocationClient locationClient;

    @BeforeEach
    void setUp() {
        entityUtilsMockedStatic = mockStatic(EntityUtils.class);
        MockitoAnnotations.openMocks(this);
        locationClient.BASE_URL = "http://example.com/api/locations";
        locationClient.rateLimiter = rateLimiter;
    }

    @Test
    void getAllEntitiesFromKudaGo_ShouldReturnLocationList_WhenRequestIsSuccessful() throws Exception {
        String jsonResponse = "[{\"name\": \"test location 1\", \"slug\": \"test slug 1\"}, " +
                "{\"name\": \"test location 2\", \"slug\": \"test slug 2\"}]";
        List<KudaGoLocationResponse> mockResponse = List.of(
                new KudaGoLocationResponse("Test location 1", "test slug 1"),
                new KudaGoLocationResponse("Test location 2", "test slug 2")
        );

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(entity);
        entityUtilsMockedStatic.when(() -> EntityUtils.toString(entity, "UTF-8")).thenReturn(jsonResponse);
        when(objectMapper.readValue(eq(jsonResponse), any(TypeReference.class))).thenReturn(mockResponse);

        List<KudaGoLocationResponse> result = locationClient.getAllEntitiesFromKudaGo();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test location 1", result.get(0).getName());
        assertEquals("test slug 1", result.get(0).getSlug());
        assertEquals("Test location 2", result.get(1).getName());
        assertEquals("test slug 2", result.get(1).getSlug());

    }

    @Test
    void getAllEntitiesFromKudaGo_ShouldThrowServiceException_WhenIOExceptionOccurs() throws Exception {
        when(httpClient.execute(any(HttpGet.class))).thenThrow(new IOException("IO error"));

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            locationClient.getAllEntitiesFromKudaGo();
        });

        assertEquals(ErrorType.INTERNAL_SERVER_ERROR.getCode(), exception.getCode());
    }

    @Test
    void getAllEntitiesFromKudaGo_ShouldRespectRateLimit() throws Exception {
        String jsonResponse = "[{\"name\": \"test location 1\", \"slug\": \"test slug 1\"}]";
        Semaphore semaphoreSpy = spy(new Semaphore(4));
        locationClient.rateLimiter = semaphoreSpy;

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(null);
        entityUtilsMockedStatic.when(() -> EntityUtils.toString(null, "UTF-8")).thenReturn(jsonResponse);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 4; i++) {
            executorService.submit(() -> {
                try {
                    locationClient.getAllEntitiesFromKudaGo();
                } catch (ServiceException e) {
                    fail("Unexpected exception: " + e.getMessage());
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        verify(httpClient, times(4)).execute(any(HttpGet.class));
        verify(semaphoreSpy, times(4)).acquire();
        verify(semaphoreSpy, times(4)).release();
    }

    @Test
    void getAllEntitiesFromKudaGo_ShouldThrowServiceException_WhenInterruptedExceptionOccurs() throws Exception {
        Semaphore semaphore = spy(new Semaphore(1));
        locationClient.rateLimiter = semaphore;

        doThrow(new InterruptedException("Thread was interrupted")).when(semaphore).acquire();

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            locationClient.getAllEntitiesFromKudaGo();
        });

        assertEquals(ErrorType.INTERNAL_SERVER_ERROR.getCode(), exception.getCode());
    }

    @AfterEach
    void tearDown() {
        entityUtilsMockedStatic.close();
    }
}
