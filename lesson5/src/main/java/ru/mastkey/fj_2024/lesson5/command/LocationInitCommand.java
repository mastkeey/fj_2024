package ru.mastkey.fj_2024.lesson5.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mastkey.fj_2024.lesson5.service.LocationService;

import java.util.concurrent.Future;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationInitCommand implements InitCommand {
    private final LocationService locationService;

    @Override
    public Future<Void> execute() {
        log.info("Executing LocationInitCommand");
        return locationService.init();
    }
}
