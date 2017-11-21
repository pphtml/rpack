package org.superbiz.rx;

import rx.Observable;

import java.time.DayOfWeek;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class DayOfWeekDelay {
    public static void main(String[] args) throws InterruptedException {
        loadRecordsFor(DayOfWeek.SUNDAY).zipWith(loadRecordsFor(DayOfWeek.MONDAY),
                (left, right) -> String.format("L%s-R%s", left, right))
                .subscribe(System.out::println);

//        Observable
//                .zip(loadRecordsFor(DayOfWeek.SUNDAY),
//                        loadRecordsFor(DayOfWeek.MONDAY),
//                        (left, right) -> String.format("L%s-R%s", left, right))
                //.just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
                //.flatMap(DayOfWeekDelay::loadRecordsFor)
                //.concatMap(DayOfWeekDelay::loadRecordsFor)
                //.subscribe(System.out::println);
        Thread.sleep(1000);
    }

    static Observable<String> loadRecordsFor(DayOfWeek dow) {
        switch(dow) {
            case SUNDAY:
                return Observable
                        .interval(90, MILLISECONDS)
                        .take(5)
                        .map(i -> "Sun-" + i);
            case MONDAY:
                return Observable
                        .interval(40, MILLISECONDS)
                        .take(5)
                        .map(i -> "Mon-" + i);
            default:
                return Observable.empty();
        }
    }
}
