package ru.mastkey.fj_2024.lesson5.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitializationService {
    private final CategoryService categoryService;
    private final LocationService locationService;

    @Qualifier("scheduledThreadPool")
    private final ScheduledExecutorService scheduledThreadPool;

    @Value("${app.init.schedule}")
    private Duration scheduleDuration;

    @EventListener(ApplicationStartedEvent.class)
    public void scheduleInit() {
        log.info("Scheduling initialization process with delay: {} seconds", scheduleDuration.toSeconds());
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            log.info("Running initAllData at {}", LocalDateTime.now());
            initAllData();
        }, 0, scheduleDuration.toSeconds(), TimeUnit.SECONDS);
    }

    public void initAllData() {
        long start = System.currentTimeMillis();
        log.info("Data initialization started");

        List<Future<Void>> futures = new ArrayList<>();

        futures.add(categoryService.init());

        futures.add(locationService.init());

        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error during data initialization", e);
                Thread.currentThread().interrupt();
                throw new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, "Error during data initialization");
            }
        }
        long duration = System.currentTimeMillis() - start;
        log.info("Data initialization completed in {} ms", duration);
    }
}
