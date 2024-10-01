package ru.mastkey.fj_2024.lesson5.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ru.mastkey.fj_2024.lesson5.client.dto.CBCurrencyResponse;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;

import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class CBClient {
    private final HttpClient httpClient;
    private final XmlMapper xmlMapper;

    @Value("${cb.currency-url}")
    public String BASE_URL;

    @Cacheable(value = "currenciesCache", key = "#root.method.name", unless = "#result == null")
    @CircuitBreaker(name = "currenciesCircuitBreaker", fallbackMethod = "fallBackMethod")
    public List<CBCurrencyResponse> getCurrencies() {
        try {
            log.info("CBClient getCurrencies");
            HttpEntity response = httpClient.execute(new HttpGet(BASE_URL)).getEntity();

            String xmlResponse = EntityUtils.toString(response, "UTF-8");

            return xmlMapper.readValue(xmlResponse, new TypeReference<List<CBCurrencyResponse>>() {
            });
        } catch (IOException e) {
            throw new ServiceException(ErrorType.SERVICE_UNAVAILABLE, "Currency service is unavailable, try again later.")
                    .withHeader("Retry-After", String.valueOf(3600));
        }
    }

    public List<CBCurrencyResponse> fallBackMethod(Throwable throwable) {
        log.error("CBClient getCurrenciesFallBackMethod");
        throw new ServiceException(ErrorType.SERVICE_UNAVAILABLE, "Currency service is unavailable, try again later.")
                .withHeader("Retry-After", String.valueOf(3600));
    }
}
