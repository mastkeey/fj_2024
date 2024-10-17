package ru.mastkey.fj_2024.lesson5.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoEventsResponse;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.Semaphore;

@Component
@RequiredArgsConstructor
public class EventClient {

    private static final String MSG_CLIENT_ERROR = "Error with kudago api request '%s'";

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    public Semaphore rateLimiter;

    @Value("${kudago.events-url}")
    public String BASE_URL;

    @Value("${app.rate-limit.max-concurrent-requests}")
    private int maxConcurrentRequests;

    @PostConstruct
    public void initSemaphore() {
        this.rateLimiter = new Semaphore(maxConcurrentRequests);
    }

    public List<KudaGoEventsResponse> getEvents(LocalDate dateFrom, LocalDate dateTo) {
        try {
            rateLimiter.acquire();
            try {
                var get = new HttpGet(BASE_URL + addParams(dateFrom, dateTo));

                var response = httpClient.execute(get).getEntity();
                String jsonResponse = EntityUtils.toString(response, "UTF-8");

                JsonNode rootNode = objectMapper.readTree(jsonResponse);
                JsonNode resultsNode = rootNode.get("results");

                return objectMapper
                        .readerForListOf(KudaGoEventsResponse.class)
                        .readValue(resultsNode);
            } finally {
                rateLimiter.release();
            }
        } catch (IOException | InterruptedException e) {
            throw new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, MSG_CLIENT_ERROR, e);
        }
    }

    public Flux<KudaGoEventsResponse> getEventsFlux(LocalDate dateFrom, LocalDate dateTo) {
        return Mono.fromCallable(() -> getEvents(dateFrom, dateTo))
                .flatMapMany(Flux::fromIterable);
    }

    private long convertLocalDateToUnixTimestamp(LocalDate date) {
        return date.atStartOfDay(ZoneId.of("UTC")).toEpochSecond();
    }

    private String addParams(LocalDate dateFrom, LocalDate dateTo) {
        return String.format("/?fields=title,price,is_free&actual_since=%s&actual_until=%s&order_by=-favorites_count",
                convertLocalDateToUnixTimestamp(dateFrom),
                convertLocalDateToUnixTimestamp(dateTo));
    }
}