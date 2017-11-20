package org.superbiz.rx;

import rx.Observable;

import java.util.concurrent.TimeUnit;

public class DelayTest {
    public static void main(String[] args) throws InterruptedException {
        Observable.just("Lorem", "ipsum", "dolor", "sit", "amet",
                        "consectetur", "adipiscing", "elit")
                .flatMap(x ->
                        Observable.just(x).delay(x.length(), TimeUnit.SECONDS))
                .subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(15);
    }
}
