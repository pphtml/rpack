package org.superbiz.rx;

import rx.Observable;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Backpressure02 {
    static <T> Observable<T> delayedCompletion() {
        return Observable.<T>empty().delay(1, SECONDS);
    }

    public static void main(String[] args) throws InterruptedException {
        Observable<String> names = Observable
                .just("Mary", "Patricia", "Linda",
                        "Barbara",
                        "Elizabeth", "Jennifer", "Maria", "Susan",
                        "Margaret", "Dorothy");
        Observable<Long> absoluteDelayMillis = Observable
                .just(0.1, 0.6, 0.9,
                        1.1,
                        3.3, 3.4, 3.5, 3.6,
                        4.4, 4.8)
                .map(d -> (long)(d * 1_000));
        Observable<String> delayedNames = names
                .zipWith(absoluteDelayMillis,
                        (n, d) -> Observable
                                .just(n)
                                .delay(d, MILLISECONDS))
                .flatMap(o -> o);
        delayedNames
                .concatWith(delayedCompletion())
                .sample(1, SECONDS)
                .subscribe(System.out::println);
        Thread.sleep(30000);
    }
}
