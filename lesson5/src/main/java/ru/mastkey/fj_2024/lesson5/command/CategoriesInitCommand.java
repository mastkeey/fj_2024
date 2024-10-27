package ru.mastkey.fj_2024.lesson5.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mastkey.fj_2024.lesson5.repository.CategoryRepository;
import ru.mastkey.fj_2024.lesson5.service.CategoryService;

import java.util.concurrent.Future;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoriesInitCommand implements InitCommand {
    private final CategoryService categoryService;

    @Override
    public Future<Void> execute() {
        log.info("Executing CategoriesInitCommand");
        return categoryService.init();
    }
}
