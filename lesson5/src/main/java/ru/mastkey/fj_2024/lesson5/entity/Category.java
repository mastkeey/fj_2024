package ru.mastkey.fj_2024.lesson5.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Accessors(chain = true)
public class Category {
    private UUID id;
    private String slug;
    private String name;
}
