package com.vinil.springreactive.eventlistener;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class TemperatureSensor {
    private final ApplicationEventPublisher publisher;              // (1)
    private final Random rnd = new Random();                        // (2)
    private final ScheduledExecutorService executor =               // (3)
            Executors.newSingleThreadScheduledExecutor();

    public TemperatureSensor(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    public void startProcessing() {                                 // (4)
        this.executor.schedule(this::probe, 1, SECONDS);
    }

    private void probe() {                                          // (5)
        double temperature = 16 + rnd.nextGaussian() * 10;
        publisher.publishEvent(new Temperature(temperature));

        // schedule the next read after some random delay (0-5 seconds)
        executor
                .schedule(this::probe, rnd.nextInt(5000), MILLISECONDS);    // (5.1)
    }
}
