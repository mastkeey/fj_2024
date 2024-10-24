package ru.mastkey.fj_2024.lesson5.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PlaceResponse {
    private UUID id;

    private String name;

    private String address;

    private String city;

    private List<EventSummary> events;
}
