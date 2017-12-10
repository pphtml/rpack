package org.superbiz.rx;

import rx.Observable;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MergeInterval {
    public static void main(String[] args) throws InterruptedException {
//        Observable
//                .interval(1000, MILLISECONDS)
//                //.publish()
//                .subscribe(System.out::println);

        Observable<String> fast = Observable.interval(10, MILLISECONDS).map(x -> "F" + x);
        Observable<String> slow = Observable.interval(117, MILLISECONDS).map(x -> "S" + x);
        slow
                .withLatestFrom(fast, (s, f) -> s + ":" + f)
                .forEach(System.out::println);

        Thread.sleep(5000);
    }
}
