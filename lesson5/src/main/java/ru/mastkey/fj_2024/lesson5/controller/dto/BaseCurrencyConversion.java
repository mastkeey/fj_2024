package ru.mastkey.fj_2024.lesson5.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class BaseCurrencyConversion {
    @NotBlank
    private String fromCurrency;
    @NotBlank
    private String toCurrency;
}