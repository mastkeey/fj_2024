package ru.mastkey.fj_2024.lesson5.controller.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ConvertCurrencyResponse extends BaseCurrencyConversion{
    private String convertedAmount;
}
