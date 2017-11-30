package org.superbiz.rx;

import rx.Observable;
import rx.schedulers.Schedulers;

public class Backpressure03 {
    public static void main(String[] args) throws InterruptedException {
        Observable
                .range(1, 1_000_000_000)
                .map(Dish::new)
                .observeOn(Schedulers.io())
                .subscribe(x -> {
                    System.out.println("Washing: " + x);
                    sleepMillis(50);
                });
        Thread.sleep(15000);
    }

    private static void sleepMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
