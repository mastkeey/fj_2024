package ru.mastkey.fj_2024.lesson5.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ConvertCurrencyRequest extends BaseCurrencyConversion {
    @Min(0)
    @NotNull
    private Double amount;
}
