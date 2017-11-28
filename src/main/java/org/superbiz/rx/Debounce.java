package org.superbiz.rx;

import rx.Observable;
import rx.observables.ConnectableObservable;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Debounce {
    public static void main(String[] args) throws InterruptedException {
//        final Observable<Long> observable = Observable
//                .interval(50, MILLISECONDS)
//                .debounce(100, MILLISECONDS)
//                .timeout(1, SECONDS);
//        observable.subscribe(System.out::println);

        ConnectableObservable<Long> upstream = Observable
                .interval(99, MILLISECONDS)
                .publish();
        upstream
                .debounce(100, MILLISECONDS)
                //.timeout(1, SECONDS, upstream.take(1));
                .timeout(1, SECONDS, upstream);
        upstream.connect();

        upstream.subscribe(System.out::println);


        Thread.sleep(5000);
    }
}
