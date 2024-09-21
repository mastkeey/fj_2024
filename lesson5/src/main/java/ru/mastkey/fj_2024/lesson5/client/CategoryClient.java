package ru.mastkey.fj_2024.lesson5.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoCategoryResponse;
import ru.mastkey.fj_2024.lesson5.entity.Category;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryClient implements ApiClient<KudaGoCategoryResponse> {

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    @Value("${kudago.category-url}")
    public String BASE_URL;

    @Override
    public List<KudaGoCategoryResponse> getAllEntitiesFromKudaGo() {

        try {
            var response = httpClient.execute(new HttpGet(BASE_URL)).getEntity();
            String jsonResponse = EntityUtils.toString(response, "UTF-8");
            return objectMapper.readValue(jsonResponse, new TypeReference<List<KudaGoCategoryResponse>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
