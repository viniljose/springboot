package com.vinil.springreactive.rxjava;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rx.Subscriber;

import java.io.IOException;

public class RxSeeEmitter extends SseEmitter {
    static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
    private final Subscriber<RxTemperature> subscriber;                // (1)

    RxSeeEmitter() {
        super(SSE_SESSION_TIMEOUT);                                   // (2)

        this.subscriber = new Subscriber<RxTemperature>() {             // (3)
            @Override
            public void onNext(RxTemperature temperature) {
                try {
                    RxSeeEmitter.this.send(temperature);                 // (4)
                } catch (IOException e) {
                    unsubscribe();                                       // (5)
                }
            }

            @Override
            public void onError(Throwable e) { }                       // (6)

            @Override
            public void onCompleted() { }                              // (7)
        };

        onCompletion(subscriber::unsubscribe);                        // (8)
        onTimeout(subscriber::unsubscribe);                           // (9)
    }

    Subscriber<RxTemperature> getSubscriber() {                        // (10)
        return subscriber;
    }
}

