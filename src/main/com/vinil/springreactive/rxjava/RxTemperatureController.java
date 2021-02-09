package com.vinil.springreactive.rxjava;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RxTemperatureController {
    private final RxTemperatureSensor temperatureSensor;                // (1)

    public RxTemperatureController(RxTemperatureSensor temperatureSensor) {
        this.temperatureSensor = temperatureSensor;
    }

    @RequestMapping(
            value = "/temperature-stream-rx",
            method = RequestMethod.GET)
    public SseEmitter events(HttpServletRequest request) {
        RxSeeEmitter emitter = new RxSeeEmitter();                     // (2)

        temperatureSensor.temperatureStream()                          // (3)
                .subscribe(emitter.getSubscriber());                        // (4)

        return emitter;                                                // (5)
    }
}
