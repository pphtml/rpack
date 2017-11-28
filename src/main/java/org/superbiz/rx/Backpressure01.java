package org.superbiz.rx;

import rx.Observable;

import java.util.concurrent.TimeUnit;

public class Backpressure01 {
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Observable
                .interval(7, TimeUnit.MILLISECONDS)
                .timestamp()
                .sample(1, TimeUnit.SECONDS)
                .map(ts -> ts.getTimestampMillis() - startTime + "ms: " + ts.getValue())
                .take(5)
                .subscribe(System.out::println);
        Thread.sleep(30000);
    }
}
