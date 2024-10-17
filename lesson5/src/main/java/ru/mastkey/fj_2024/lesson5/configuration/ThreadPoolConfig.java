package ru.mastkey.fj_2024.lesson5.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ThreadPoolConfig {

    @Value("${app.threadpool.size}")
    private int threadPoolSize;

    @Value("${app.scheduled.threadpool.size}")
    private int scheduledThreadPoolSize;

    @Bean(name = "fixedThreadPool")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(threadPoolSize, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("FixedThreadPool-Worker");
            return thread;
        });
    }

    @Bean(name = "scheduledThreadPool")
    public ScheduledExecutorService scheduledThreadPool() {
        return Executors.newScheduledThreadPool(scheduledThreadPoolSize, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("ScheduledThreadPool-Worker");
            return thread;
        });
    }
}