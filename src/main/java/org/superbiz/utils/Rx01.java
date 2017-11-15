package org.superbiz.utils;

import rx.Observable;

public class Rx01 {
    public static void main(String[] args) {
        final Observable<String> observable = Observable.<Integer>create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onCompleted();
        })
//                .doOnNext(i -> System.out.println(Thread.currentThread()))
//                .filter(i -> i % 2 == 0)
                .map(i -> "Value " + i + " processed on " + Thread.currentThread());

        observable
                .subscribe(s -> System.out.println("SOME VALUE =>" + s));
        observable.subscribe(s -> System.out.println("SOME VALUE =>" + s));
        System.out.println("Will print BEFORE values are emitted");
    }
}
