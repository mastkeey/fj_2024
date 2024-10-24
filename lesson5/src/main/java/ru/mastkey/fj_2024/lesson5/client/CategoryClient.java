package ru.mastkey.fj_2024.lesson5.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoCategoryResponse;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

@Component
@RequiredArgsConstructor
public class CategoryClient implements ApiClient<KudaGoCategoryResponse> {

    private static final String MSG_CLIENT_ERROR = "Error with kudago api request '%s'";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    public Semaphore rateLimiter;

    @Value("${kudago.category-url}")
    public String BASE_URL;

    @Value("${app.rate-limit.max-concurrent-requests}")
    private int maxConcurrentRequests;

    @PostConstruct
    public void initSemaphore() {
        this.rateLimiter = new Semaphore(maxConcurrentRequests);
    }

    @Override
    public List<KudaGoCategoryResponse> getAllEntitiesFromKudaGo() {

        try {
            rateLimiter.acquire();
            try {
                var response = httpClient.execute(new HttpGet(BASE_URL)).getEntity();
                String jsonResponse = EntityUtils.toString(response, "UTF-8");
                return objectMapper.readValue(jsonResponse, new TypeReference<List<KudaGoCategoryResponse>>() {
                });
            } finally {
                rateLimiter.release();
            }
        } catch (IOException | InterruptedException e) {
            throw new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, MSG_CLIENT_ERROR, e);
        }
    }
}
