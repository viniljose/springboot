package com.vinil.springreactive.reactor;

import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Test {
    public static void main(String[] args) {

        //This generates Out of Memory Error.
        /*Flux.range(1, 100)                                                  // (1)
                .repeat()                                                        // (2)
                .collectList()                                                   // (3)
                .block();*/
        //CompletableFuture Sample
        Test tst = new Test();
        try {
            Future<String> completableFuture = tst.calculateAsync();
            String result = completableFuture.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.complete("Hello");
            return null;
        });

        return completableFuture;
    }
}
