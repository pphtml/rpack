package org.superbiz.rx;

import rx.Observable;

public class RangeTest {
    public static void main(String[] args) {
        final Observable<Integer> observable = Observable.range(1, 5).takeLast(2);
        observable.subscribe(System.out::println);
    }
}
