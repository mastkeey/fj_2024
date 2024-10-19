package ru.mastkey.fj_2024.lesson5.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EventResponse {
    private UUID id;

    private String name;

    private String date;

    private PlaceSummary place;
}
