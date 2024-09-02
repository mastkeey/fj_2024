package ru.mastkey.fj_2024.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class City {
    @JsonProperty("slug")
    private String slug;

    @JsonProperty("coords")
    private Coordinates coordinates;
}
