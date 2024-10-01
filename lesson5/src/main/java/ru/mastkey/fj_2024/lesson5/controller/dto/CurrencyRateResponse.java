package ru.mastkey.fj_2024.lesson5.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrencyRateResponse {
    private String currency;
    private String rate;
    private String name;
}
