package com.vinil.springreactive.reactor;

import reactor.core.publisher.Flux;

public class Test {
    public static void main(String[] args) {

        //This generates Out of Memory Error.
        Flux.range(1, 100)                                                  // (1)
                .repeat()                                                        // (2)
                .collectList()                                                   // (3)
                .block();
    }
}
