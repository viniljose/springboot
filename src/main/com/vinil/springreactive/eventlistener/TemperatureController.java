package com.vinil.springreactive.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class TemperatureController {
    private final Set<SseEmitter> clients =                          // (1)
            new CopyOnWriteArraySet<>();

    @RequestMapping(
            value = "/temperature-stream",                                // (2)
            method = RequestMethod.GET)
    public SseEmitter events(HttpServletRequest request) {           // (3)
        SseEmitter emitter = new SseEmitter();                        // (4)
        clients.add(emitter);                                         // (5)

        // Remove emitter from clients on error or disconnect
        emitter.onTimeout(() -> clients.remove(emitter));             // (6)
        emitter.onCompletion(() -> clients.remove(emitter));          // (7)

        return emitter;                                               // (8)
    }
    @Async                                                           // (9)
    @EventListener                                                   // (10)
    public void handleMessage(Temperature temperature) {             // (11)
        List<SseEmitter> deadEmitters = new ArrayList<>();            // (12)
        clients.forEach(emitter -> {
            try {
                emitter.send(temperature, MediaType.APPLICATION_JSON);  // (13)
            } catch (Exception ignore) {
                deadEmitters.add(emitter);                              // (14)
            }
        });
        clients.removeAll(deadEmitters);                              // (15)
    }
}
