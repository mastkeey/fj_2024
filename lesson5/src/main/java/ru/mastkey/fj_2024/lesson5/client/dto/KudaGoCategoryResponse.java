package ru.mastkey.fj_2024.lesson5.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KudaGoCategoryResponse {
    private String name;
    private String slug;
}